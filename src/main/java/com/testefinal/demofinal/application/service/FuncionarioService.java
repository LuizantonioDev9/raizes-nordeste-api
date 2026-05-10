package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.domain.enums.Perfil;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.model.Funcionario;
import com.testefinal.demofinal.domain.model.Unidade;
import com.testefinal.demofinal.infrastructure.repository.FuncionarioRepository;
import com.testefinal.demofinal.infrastructure.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    private final UnidadeRepository unidadeRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, UnidadeRepository unidadeRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.unidadeRepository = unidadeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Funcionario cadastrar(Funcionario funcionario) {

        if(funcionarioRepository.findByEmail(funcionario.getEmail()).isPresent()) {
            throw new ConflitoException("Email já cadastrado");
        }

        Unidade unidade = unidadeRepository.findById(funcionario.getUnidade().getId())
                .orElseThrow(() -> new NaoEncontradoException("Unidade não encontrada"));

        funcionario.setUnidade(unidade);
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));

        if (funcionario.getPerfil() == null) {
            funcionario.setPerfil(Perfil.FUNCIONARIO);
        }

        return funcionarioRepository.save(funcionario);
    }
}

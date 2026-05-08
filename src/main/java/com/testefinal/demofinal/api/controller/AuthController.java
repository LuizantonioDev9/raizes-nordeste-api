package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.LoginDTO;
import com.testefinal.demofinal.api.DTO.TokenResponseDTO;
import com.testefinal.demofinal.api.DTO.UsuarioResumoDTO;
import com.testefinal.demofinal.application.service.JwtService;
import com.testefinal.demofinal.domain.exception.EmailSenhaInvalidoException;
import com.testefinal.demofinal.domain.exception.NaoAutorizadoException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.domain.model.Funcionario;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import com.testefinal.demofinal.infrastructure.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthController(ClienteRepository clienteRepository, FuncionarioRepository funcionarioRepository,PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO dto) {

        var clienteOpt = clienteRepository.findByEmail(dto.email());
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            validarSenha(dto.senha(), cliente.getSenha());

            String token = gerarToken(cliente.getEmail(), cliente.getSenha(), cliente.getPerfil().name());
            UsuarioResumoDTO usuarioLogado = new UsuarioResumoDTO(cliente.getId(),cliente.getNome(), cliente.getPerfil().name());
            return ResponseEntity.ok(new TokenResponseDTO(token, "Bearer", 3600, usuarioLogado));

        }

        var funcionarioOpt = funcionarioRepository.findByEmail(dto.email());
        if (funcionarioOpt.isPresent()) {
            Funcionario funcionario = funcionarioOpt.get();
            validarSenha(dto.senha(), funcionario.getSenha());

            String token = gerarToken(funcionario.getEmail(), funcionario.getSenha(), funcionario.getPerfil().name());
            UsuarioResumoDTO usuarioLogado = new UsuarioResumoDTO(funcionario.getId(),funcionario.getNome(), funcionario.getPerfil().name());

            return ResponseEntity.ok(new TokenResponseDTO(token, "Bearer", 3600, usuarioLogado));
        }
        throw new NaoAutorizadoException("E-mail ou senha invalidos");
    }

    private void validarSenha(String senhaInformada, String senhaBanco) {
        if (!passwordEncoder.matches(senhaInformada, senhaBanco)) {
            throw new NaoAutorizadoException("E-mail ou senha invalidos");
        }
    }

    private String gerarToken(String email, String senha, String perfil) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(senha)
                .authorities("ROLE_" + perfil)
                .build();

        return jwtService.generateToken(userDetails);
    }

}

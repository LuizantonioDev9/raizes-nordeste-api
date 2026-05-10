package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.ClienteResponseDTO;
import com.testefinal.demofinal.domain.enums.Perfil;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.infrastructure.integration.log.LogService;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder,LogService logService) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    @Transactional
    public Cliente cadastrarCliente(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());

        if (clienteExistente.isPresent()) {
            throw new ConflitoException("Já existe um cliente cadastrado com esse e-mail");
        }

        if(!Boolean.TRUE.equals(cliente.getAceitaPoliticaPrivacidade())) {
            throw new NegocioException("É necessário aceitar a política de privacidade para realizar o cadastro de cliente");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setPerfil(Perfil.CLIENTE);
        cliente.setDataConsentimentoPrivacidade(LocalDateTime.now());

        logService.auditoria("Cliente realizou o aceite da politica de privacidade. clienteId = " + cliente.getId());

        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente atualizarCliente(UUID id, Cliente dadosAtualizados) {
        Cliente clienteAtual = buscarPorId(id);

        clienteAtual.setNome(dadosAtualizados.getNome());
        clienteAtual.setTelefone(dadosAtualizados.getTelefone());

        //valida se o email novo atualizado já está em uso por cliente
        if(!clienteAtual.getEmail().equals(dadosAtualizados.getEmail())) {
            if(clienteRepository.findByEmail(dadosAtualizados.getEmail()).isPresent()) {
                throw new ConflitoException("Este e-mail já está em uso por outro cliente");
            }
            clienteAtual.setEmail(dadosAtualizados.getEmail());
        }
        return clienteRepository.save(clienteAtual);
    }

    @Transactional
    public ClienteResponseDTO buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
        );
    }

    @Transactional
    public void removerCliente(UUID id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

}

package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.domain.enums.Perfil;
import com.testefinal.demofinal.domain.exception.ClienteNaoEncontrado;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente cadastrarCliente(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());

        if (clienteExistente.isPresent()) {
            throw new ConflitoException("Já existe um cliente cadastrado com esse e-mail");
        }
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setPerfil(Perfil.CLIENTE);

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
    public void removerCliente(UUID id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

}

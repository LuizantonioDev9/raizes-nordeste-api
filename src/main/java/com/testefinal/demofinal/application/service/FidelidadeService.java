package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.domain.exception.ClienteNaoEncontrado;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.domain.model.Pedido;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import com.testefinal.demofinal.infrastructure.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FidelidadeService {

    private final ClienteRepository clienteRepository;

    public FidelidadeService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public void registrarConsentimento(UUID clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));

        if (Boolean.TRUE.equals(cliente.getParticiparFidelidade())) {
            throw new ConflitoException("Cliente já participa do programa de fidelidade");
        }

        cliente.setParticiparFidelidade(true);
        cliente.setDataConsentimentoFidelidade(LocalDateTime.now());
        cliente.setSaldoPontos(50);

        clienteRepository.save(cliente);

    }

    public SaldoPontosDTO consultarMeusPontos() {
        String email = getUsuarioLogado();
        Cliente cliente = buscarPorEmail(email);
        return new SaldoPontosDTO(cliente.getSaldoPontos());

    }

    private String getUsuarioLogado() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado com o e-mail informado " + email));
    }

}

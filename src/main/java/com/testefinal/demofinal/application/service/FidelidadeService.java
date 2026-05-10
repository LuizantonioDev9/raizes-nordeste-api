package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.domain.exception.ConflitoException;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.infrastructure.integration.log.LogService;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FidelidadeService {

    private final ClienteRepository clienteRepository;
    private final LogService logService;

    public FidelidadeService(ClienteRepository clienteRepository, LogService logService) {
        this.clienteRepository = clienteRepository;
        this.logService = logService;
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

        logService.auditoria("Cliente aderiu ao programa de fidelidade. clienteId="
                + cliente.getId()
                + ", usuario=" + getUsuarioLogado());

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

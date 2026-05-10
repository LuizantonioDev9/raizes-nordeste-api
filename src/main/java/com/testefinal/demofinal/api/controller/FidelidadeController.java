package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.application.service.FidelidadeService;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    private final FidelidadeService fidelidadeService;
    private final ClienteRepository clienteRepository;


    public FidelidadeController(FidelidadeService fidelidadeService, ClienteRepository clienteRepository) {
        this.fidelidadeService = fidelidadeService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/aderir/{clienteId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> aderir(@PathVariable UUID clienteId) {
        fidelidadeService.registrarConsentimento(clienteId);
        return ResponseEntity.ok("Consentimento registrado com sucesso! Bem vindo ao nosso programa de fidelidade");
    }


    @GetMapping("/meus-pontos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<SaldoPontosDTO> consultarMeusPontos() {
        SaldoPontosDTO saldo = fidelidadeService.consultarMeusPontos();
        return ResponseEntity.ok(saldo);
    }



}

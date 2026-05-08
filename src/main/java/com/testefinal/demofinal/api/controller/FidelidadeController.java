package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.application.service.ClienteService;
import com.testefinal.demofinal.application.service.FidelidadeService;
import com.testefinal.demofinal.application.service.PedidoService;
import com.testefinal.demofinal.domain.exception.ClienteNaoEncontrado;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import com.testefinal.demofinal.infrastructure.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
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
    public ResponseEntity<String> aderir(@PathVariable UUID clienteId) {
        fidelidadeService.registrarConsentimento(clienteId);
        return ResponseEntity.ok("Consentimento registrado com sucesso! Bem vindo ao nosso programa de fidelidade");
    }

    @GetMapping("/saldo/{clienteId}")
    public ResponseEntity<Map<String, Object>> consultarSaldo(@PathVariable UUID clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNaoEncontrado("Cliente não encontrado"));

        //1 ponto = R$ 0,05
        BigDecimal valorDesconto = BigDecimal.valueOf(cliente.getSaldoPontos())
                .multiply(new BigDecimal("0.05"));

        return ResponseEntity.ok(Map.of(
                "pontos", cliente.getSaldoPontos(),
                "valorDesconto", valorDesconto,
                "podeResgatar", cliente.getSaldoPontos() >= 100 // Ex: só resgata a partir de 100 pontos
        ));
    }

    @GetMapping("/meus-pontos")
    public ResponseEntity<SaldoPontosDTO> consultarMeusPontos() {
        //chamamos o serviço que já sabe quem é o utilizador logado pelo token
        SaldoPontosDTO saldo = fidelidadeService.consultarMeusPontos();
        return ResponseEntity.ok(saldo);
    }



}

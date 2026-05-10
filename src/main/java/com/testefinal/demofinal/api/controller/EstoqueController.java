package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.EstoqueRequestDTO;
import com.testefinal.demofinal.application.service.EstoqueService;
import com.testefinal.demofinal.domain.model.Estoque;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {


    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estoque> cadastrar(@RequestBody EstoqueRequestDTO estoque) {
        Estoque estoqueSalvo = estoqueService.cadastrarEstoque(
                estoque.produtoId(),
                estoque.unidadeId(),
                estoque.quantidade()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueSalvo);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<Estoque> buscarEstoque(@RequestParam UUID produtoId, @RequestParam UUID unidadeId) {
        return ResponseEntity.ok(estoqueService.buscarEstoque(produtoId, unidadeId));
    }

    @PostMapping("/saidas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saidaEstoque(@RequestBody @Valid EstoqueRequestDTO estoqueRequest) {
        estoqueService.saidaEstoque(
                estoqueRequest.produtoId(),
                estoqueRequest.unidadeId(),
                estoqueRequest.quantidade()
        );
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/entradas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> entradaEstoque(@RequestBody @Valid EstoqueRequestDTO estoqueRequest) {
        estoqueService.entradaEstoque(
                estoqueRequest.produtoId(),
                estoqueRequest.unidadeId(),
                estoqueRequest.quantidade()
        );
        return ResponseEntity.noContent().build();
    }
}

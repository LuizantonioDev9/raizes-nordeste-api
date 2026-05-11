package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.CatalogoUnidadeResponseDTO;
import com.testefinal.demofinal.api.DTO.UnidadeDTO;
import com.testefinal.demofinal.application.service.EstoqueService;
import com.testefinal.demofinal.application.service.UnidadeService;
import com.testefinal.demofinal.domain.model.Unidade;
import com.testefinal.demofinal.infrastructure.repository.UnidadeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;
    private final EstoqueService estoqueService;

    public UnidadeController(UnidadeService unidadeService, EstoqueService estoqueService) {
        this.unidadeService = unidadeService;
        this.estoqueService = estoqueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Unidade> criarUnidade(@RequestBody @Valid UnidadeDTO unidade) {
        Unidade unidadeCriada = unidadeService.cadastrarUnidade(unidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeCriada);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Unidade>> listarUnidades() {
        return ResponseEntity.ok(unidadeService.listarUnidades());
    }

    @GetMapping("/{id}/produtos")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<CatalogoUnidadeResponseDTO> listarProdutosDaUnidade(@PathVariable UUID id) {
        return ResponseEntity.ok(estoqueService.listarProdutosPorUnidade(id));
    }
}

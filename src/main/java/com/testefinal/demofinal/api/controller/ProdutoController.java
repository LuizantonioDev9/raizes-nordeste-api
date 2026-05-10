package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.ProdutoRequestDTO;
import com.testefinal.demofinal.application.service.ProdutoService;
import com.testefinal.demofinal.domain.model.Produto;
import com.testefinal.demofinal.infrastructure.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> criar(@RequestBody @Valid ProdutoRequestDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.nome());
        produto.setPreco(produtoDTO.preco());

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(produto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok(produtoService.listarProdutos());
    }

}

package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.EstoqueRequestDTO;
import com.testefinal.demofinal.application.service.EstoqueService;
import com.testefinal.demofinal.domain.model.Estoque;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Estoque> cadastrar(@RequestBody EstoqueRequestDTO estoque) {
        Estoque estoqueSalvo = estoqueService.cadastrarEstoque(
                estoque.produtoId(),
                estoque.unidadeId(),
                estoque.quantidade()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueSalvo);
    }

    @GetMapping
    public ResponseEntity<Estoque> buscarEstoque(@RequestParam UUID produtoId, @RequestParam UUID unidadeId) {
        return ResponseEntity.ok(estoqueService.buscarEstoque(produtoId, unidadeId));
    }

    @PutMapping("/saida")
    public ResponseEntity<String> diminuirEstoque(@RequestBody EstoqueRequestDTO estoqueRequest) {
        estoqueService.diminuirEstoque(
                estoqueRequest.produtoId(),
                estoqueRequest.unidadeId(),
                estoqueRequest.quantidade()
        );
        return ResponseEntity.ok("Produto retirado do Estoque");
    }


    @PutMapping("/entrada")
    public ResponseEntity<String> adicionarAoEstoque(@RequestBody EstoqueRequestDTO estoqueRequest) {
        estoqueService.adicionarAoEstoque(
                estoqueRequest.produtoId(),
                estoqueRequest.unidadeId(),
                estoqueRequest.quantidade());
        return ResponseEntity.ok("Produto adicionado ao Estoque");
    }
}

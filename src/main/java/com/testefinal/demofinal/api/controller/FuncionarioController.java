package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.application.service.FuncionarioService;
import com.testefinal.demofinal.domain.model.Funcionario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Funcionario> cadastrar(@RequestBody Funcionario funcionario) {
        Funcionario funcionarioSalvo = funcionarioService.cadastrar(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);
    }

}

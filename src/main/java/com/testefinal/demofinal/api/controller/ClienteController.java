package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.ClienteRequestDTO;
import com.testefinal.demofinal.api.DTO.ClienteResponseDTO;
import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.application.service.ClienteService;
import com.testefinal.demofinal.domain.model.Cliente;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("clientes")
public class ClienteController {

    private ClienteService clienteService;


    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody @Valid ClienteRequestDTO clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.nome());
        cliente.setEmail(clienteRequest.email());
        cliente.setTelefone(clienteRequest.telefone());
        cliente.setSenha(clienteRequest.senha());
        cliente.setAceitaPoliticaPrivacidade(clienteRequest.aceitePoliticaPrivacidade());

        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(clienteSalvo));
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@RequestParam String email) {
        ClienteResponseDTO cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    private ClienteResponseDTO toDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
        );
    }


}


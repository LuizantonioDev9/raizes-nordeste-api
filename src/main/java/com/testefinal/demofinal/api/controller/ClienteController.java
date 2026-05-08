package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.ClienteRequestDTO;
import com.testefinal.demofinal.api.DTO.ClienteResponseDTO;
import com.testefinal.demofinal.api.DTO.SaldoPontosDTO;
import com.testefinal.demofinal.application.service.ClienteService;
import com.testefinal.demofinal.domain.model.Cliente;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody ClienteRequestDTO clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.nome());
        cliente.setEmail(clienteRequest.email());
        cliente.setTelefone(clienteRequest.telefone());
        cliente.setSenha(clienteRequest.senha());

        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(clienteSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarCliente(@PathVariable UUID id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(cliente));
    }


    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        List<ClienteResponseDTO> lista = clienteService.listarTodos().stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable UUID id, @RequestBody ClienteRequestDTO clienteRequest){
        Cliente dadosAtualizados = new Cliente();
        dadosAtualizados.setNome(clienteRequest.nome());
        dadosAtualizados.setEmail(clienteRequest.email());
        dadosAtualizados.setTelefone(clienteRequest.telefone());

        Cliente clienteAtualizado = clienteService.atualizarCliente(id, dadosAtualizados);
        return ResponseEntity.ok(toDTO(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        clienteService.removerCliente(id);
        return ResponseEntity.noContent().build();
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


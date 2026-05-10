package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.*;
import com.testefinal.demofinal.application.service.PedidoService;
import com.testefinal.demofinal.domain.enums.CanalPedido;
import com.testefinal.demofinal.domain.model.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<Object> criar(@RequestBody @Valid NovoPedidoRequestDTO novoPedidoRequest) {
        Pedido pedido = new Pedido();

        if(novoPedidoRequest.unidadeId() != null){
            Unidade unidade = new Unidade();
            unidade.setId(novoPedidoRequest.unidadeId());
            pedido.setUnidade(unidade);
        }

        pedido.setCanalPedido(novoPedidoRequest.canalPedido().name());

        if(novoPedidoRequest.codigoCupom() != null){
            Cupom cupom = new Cupom();
            cupom.setCodigo(novoPedidoRequest.codigoCupom());
            pedido.setCupom(cupom);
        }

        if (novoPedidoRequest.itens() != null && !novoPedidoRequest.itens().isEmpty()) {
            for (ItemPedidoRequestDTO itemDto : novoPedidoRequest.itens()) {
                if (itemDto.produtoId() == null) {
                    return ResponseEntity.badRequest().build();
                }

                ItemPedido item = new ItemPedido();

                Produto produto = new Produto();
                produto.setId(itemDto.produtoId());

                item.setProduto(produto);
                item.setQuantidade(itemDto.quantidade());
                item.setPedido(pedido);

                pedido.getItens().add(item);
            }
        }

        if(novoPedidoRequest.pontosResgate() != null){
            pedido.setPontosResgate(novoPedidoRequest.pontosResgate());
        }

        Pedido novoPedido = pedidoService.criarPedido(pedido);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoPedido.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pedidoService.toDTO(novoPedido));
    }

    @PostMapping("/{id}/itens")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> adicionarItem(@PathVariable UUID id,
                                                @RequestBody ItemPedidoRequestDTO itemRequest) {
        ItemPedido item = new ItemPedido();

        Produto produto = new Produto();
        produto.setId(itemRequest.produtoId());
        item.setProduto(produto);

        item.setQuantidade(itemRequest.quantidade());

        Pedido pedidoAtualizado = pedidoService.adicionarItem(id, item);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.toDTO(pedidoAtualizado));
    }


    @PostMapping("/{id}/cupom")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> aplicarCupom(@PathVariable UUID id, @RequestBody @Valid CupomRequestDTO cupomRequest) {
        Pedido pedidoAtualizado = pedidoService.aplicarCupom(id, cupomRequest.codigo());
        return ResponseEntity.ok(pedidoService.toDTO(pedidoAtualizado));
    }

    @GetMapping("/meu-carrinho")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> meuCarrinho() {
        PedidoResponseDTO carrinho = pedidoService.buscarMeuCarrinho();
        return ResponseEntity.ok(carrinho);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorCanal(@RequestParam(required = false)CanalPedido canalPedido) {
        List<PedidoResponseDTO> filtrados = pedidoService.buscarPorCanal(canalPedido);
        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedidoService.toDTO(pedido));
    }

    @PutMapping("/{id}/preparar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> preparar(@PathVariable UUID id) {
        Pedido pedido = pedidoService.preparar(id);
        return ResponseEntity.ok(pedidoService.toDTO(pedido));
    }

    @PutMapping("/{id}/pronto")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> pronto(@PathVariable UUID id) {
        Pedido pedido = pedidoService.marcarComoPronto(id);
        return ResponseEntity.ok(pedidoService.toDTO(pedido));
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> finalizar(@PathVariable UUID id) {
        Pedido pedido = pedidoService.finalizarPedido(id);
        return ResponseEntity.ok(pedidoService.toDTO(pedido));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN', 'FUNCIONARIO')")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable UUID id) {
        Pedido pedidoCancelado = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedidoService.toDTO(pedidoCancelado));
    }



}

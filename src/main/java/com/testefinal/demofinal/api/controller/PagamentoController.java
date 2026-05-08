package com.testefinal.demofinal.api.controller;

import com.testefinal.demofinal.api.DTO.ItemResponseDTO;
import com.testefinal.demofinal.api.DTO.PagamentoRequestDTO;
import com.testefinal.demofinal.api.DTO.PagamentoResponseDTO;
import com.testefinal.demofinal.api.DTO.PedidoResponseDTO;
import com.testefinal.demofinal.application.service.PagamentoService;
import com.testefinal.demofinal.domain.model.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

//    @PostMapping("/mock-confirmacao")
//    public ResponseEntity<PedidoResponseDTO> confirmarPagamentoMock(@RequestBody PagamentoRequestDTO request) {
//
//        Pedido pedido = pagamentoService.confirmarPagamentoMock(request.pedidoId(), request.sucesso());
//
//
//        List<ItemResponseDTO> itensDTO = pedido.getItens().stream()
//                .map(item -> new ItemResponseDTO(
//                        item.getProduto().getNome(),
//                        item.getQuantidade(),
//                        item.getValor()
//                )).toList();
//
//        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
//                pedido.getId(),
//                pedido.getCanalPedido(),
//                pedido.getStatus().name(),
//                pedido.getTotal(),
//                pedido.getCupom() != null ? pedido.getCupom().getCodigo() : null,
//                pedido.getDataCriacao(),
//                pedido.getDataAtualizacao(),
//                itensDTO
//        );
//
//        return ResponseEntity.ok(responseDTO);
//    }

    @PostMapping("/mock-confirmacao")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamentoMock(@RequestBody PagamentoRequestDTO request) {

        PagamentoResponseDTO resposta = pagamentoService.confirmarPagamentoMock(
                request.pedidoId(),
                request.sucesso()
        );

        return ResponseEntity.ok(resposta);
    }
}

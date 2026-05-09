package com.testefinal.demofinal.application.service;

import com.testefinal.demofinal.api.DTO.PagamentoResponseDTO;
import com.testefinal.demofinal.domain.enums.StatusPagamento;
import com.testefinal.demofinal.domain.enums.StatusPedido;
import com.testefinal.demofinal.domain.exception.NaoEncontradoException;
import com.testefinal.demofinal.domain.exception.NegocioException;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.domain.model.ItemPedido;
import com.testefinal.demofinal.domain.model.Pagamento;
import com.testefinal.demofinal.domain.model.Pedido;
import com.testefinal.demofinal.infrastructure.repository.ClienteRepository;
import com.testefinal.demofinal.infrastructure.repository.PagamentoRepository;
import com.testefinal.demofinal.infrastructure.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagamentoService {


    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EstoqueService estoqueService;
    private final ClienteRepository clienteRepository;


    public PagamentoService(PedidoRepository pedidoRepository, PagamentoRepository pagamentoRepository, EstoqueService estoqueService, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.estoqueService = estoqueService;
        this.clienteRepository = clienteRepository;
    }

    //simula um pagamento aprovado
    public boolean processarPagamento(Pedido pedido, boolean simularSucesso) {
        return simularSucesso;
    }

    @Transactional
    public PagamentoResponseDTO confirmarPagamentoMock(UUID pedidoId, boolean simularSucesso) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido nao encontrado"));

        if (pedido.getItens().isEmpty()) {
            throw new NegocioException("Pedido vazio não pode ser pago");
        }

        if (pedido.getStatus() != StatusPedido.CRIADO &&
                pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new NegocioException("Pedido não disponivel para pagamento");
        }

        if (pagamentoRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new NegocioException("Pagamento já registrado para este pedido");
        }

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.validarEstoque(
                    item.getProduto().getId(),
                    pedido.getUnidade().getId(),
                    item.getQuantidade()
            );
        }

        boolean aprovado = processarPagamento(pedido, simularSucesso);


        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setMetodo("GATEWAY_MOCK");
        pagamento.setValor(pedido.getTotal());
        pagamento.setDataDoPagamento(LocalDateTime.now());

        if (aprovado) {
            for (ItemPedido item : pedido.getItens()) {
                estoqueService.saidaEstoque(
                        item.getProduto().getId(),
                        pedido.getUnidade().getId(),
                        item.getQuantidade()
                );
            }


            if (pedido.getCliente() != null) {
                aplicarPontosFidelidade(pedido);
            }

            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setPayloadRetorno("{\"status\":\"APROVADO\",\"mensagem\":\"Pagamento mock aprovado\"}");
            pedido.setStatus(StatusPedido.PAGO);
        } else {
            pagamento.setStatus(StatusPagamento.RECUSADO);
            pagamento.setPayloadRetorno("{\"status\":\"RECUSADO\",\"mensagem\":\"Pagamento mock recusado\"}");
            pedido.setStatus(StatusPedido.RECUSADO);
        }

        pagamentoRepository.save(pagamento);
        pedidoRepository.save(pedido);

        return new PagamentoResponseDTO(
                pagamento.getId(),
                pedido.getId(),
                pagamento.getStatus() != null ? pagamento.getStatus().name() : "RECUSADO",
                pagamento.getMetodo(),
                pagamento.getValor(),
                pagamento.getPayloadRetorno(),
                pagamento.getDataDoPagamento()
        );
    }

    private void aplicarPontosFidelidade(Pedido pedido) {
        Cliente cliente = pedido.getCliente();

        if (Boolean.TRUE.equals(cliente.getParticiparFidelidade())) {
            int pontosGanhos = pedido.getTotal().intValue();
            int saldoAtual = (cliente.getSaldoPontos() != null) ? cliente.getSaldoPontos() : 0;

            cliente.setSaldoPontos(saldoAtual + pontosGanhos);
            clienteRepository.save(cliente);
        }
    }
}

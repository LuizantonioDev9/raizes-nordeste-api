package com.testefinal.demofinal.application.service;


import com.testefinal.demofinal.api.DTO.ItemResponseDTO;
import com.testefinal.demofinal.api.DTO.PedidoResponseDTO;
import com.testefinal.demofinal.domain.enums.CanalPedido;
import com.testefinal.demofinal.domain.enums.StatusPedido;
import com.testefinal.demofinal.domain.exception.*;
import com.testefinal.demofinal.domain.model.*;
import com.testefinal.demofinal.infrastructure.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ClienteRepository clienteRepository;

    private final UnidadeRepository unidadeRepository;

    private final ProdutoRepository produtoRepository;

    private final CupomService cupomService;

    private final CupomRepository cupomRepository;

    private final EstoqueService estoqueService;


    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         UnidadeRepository unidadeRepository,
                         ProdutoRepository produtoRepository,
                         CupomService cupomService,
                         CupomRepository cupomRepository,
                         EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.cupomService = cupomService;
        this.cupomRepository = cupomRepository;

        this.estoqueService = estoqueService;
    }

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        validarCanalPedido(pedido);
        validarCliente(pedido);
        validarUnidade(pedido);
        validarCupom(pedido);
        validarItensPedido(pedido);

        recalcular(pedido);

        validarProgramaFidelidade(pedido);

        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setDataCriacao(LocalDateTime.now());

        return pedidoRepository.save(pedido);

    }

    private void validarCanalPedido(Pedido pedido) {
        if (pedido.getCanalPedido() == null) {
            throw new ValidaRegraException("Canal do pedido é obrigatório");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String permissoes = auth.getAuthorities().toString();

        if (permissoes.contains("ROLE_CLIENTE") && "BALCAO".equalsIgnoreCase(pedido.getCanalPedido())) {
            throw new NegocioException("Clientes não podem realizar pedidos via canal BALCAO");
        }
    }

    private void validarCliente(Pedido pedido) {
        if(pedido.getCliente() != null && pedido.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                    .orElseThrow(() -> new NaoEncontradoException("Cliente informado não foi encontrado"));
            pedido.setCliente(cliente);
        }
        else {
            String email = getUsuarioLogado();
            Optional<Cliente> clienteLogado = clienteRepository.findByEmail(email);

            if (clienteLogado.isPresent()) {
                pedido.setCliente(clienteLogado.get());
            }
            else {
                pedido.setCliente(null);
            }
        }
    }

    private void validarUnidade(Pedido pedido) {
        if (pedido.getUnidade() != null && pedido.getUnidade().getId() != null) {
            Unidade unidade = unidadeRepository.findById(pedido.getUnidade().getId())
                    .orElseThrow(() -> new NaoEncontradoException("Unidade não encontrada"));
            pedido.setUnidade(unidade);
        } else {
            throw new ValidaRegraException("Unidade do pedido é obrigatoria");
        }


    }

    private void validarCupom(Pedido pedido) {
        if (pedido.getCupom() != null && pedido.getCupom().getCodigo() != null) {
            Cupom cupom = cupomService.buscarPorCodigo(pedido.getCupom().getCodigo());
            pedido.setCupom(cupom);
        }
    }

    private void validarItensPedido(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new NegocioException("O pedido deve possuir pelo menos um item.");
        }

        List<ItemPedido> itensDoRequest = new java.util.ArrayList<>(pedido.getItens());
        pedido.getItens().clear();

        for (ItemPedido item : itensDoRequest) {
            processarNovoItem(pedido, item);
        }
    }

    public void validarProgramaFidelidade(Pedido pedido) {
        if (pedido.getPontosResgate() != null && pedido.getPontosResgate() > 0) {

            Cliente cliente = pedido.getCliente();

            if (cliente == null) {
                throw new NegocioException("É necessario identificar o cliente para os pontos de fidelidade");
            }

            if (!Boolean.TRUE.equals(cliente.getParticiparFidelidade())) {
                throw new NegocioException(("O cliente não participa do programa de fidelidade"));
            }

            if (cliente.getSaldoPontos() == null || cliente.getSaldoPontos() < pedido.getPontosResgate()) {
                throw new NegocioException("Saldo de pontos insuficiente para realizar este resgate.");
            }

            BigDecimal desconto = BigDecimal.valueOf(pedido.getPontosResgate() * 0.05);

            if (desconto.compareTo(pedido.getTotal()) > 0) {
                throw new NegocioException("O valor do desconto (R$ " + desconto + ") é maior que o total do pedido (R$ " + pedido.getTotal() + "). Use menos pontos!");
            }

            pedido.setTotal(pedido.getTotal().subtract(desconto));
            cliente.setSaldoPontos(cliente.getSaldoPontos() - pedido.getPontosResgate());
        }
    }



    private void processarNovoItem(Pedido pedido, ItemPedido item) {
        System.out.println("ENTROU EM PROCESSAR ITEM");
        System.out.println("ITEM: " + item);
        System.out.println("PRODUTO: " + item.getProduto());
        System.out.println("PRODUTO ID: " + (item.getProduto() != null ? item.getProduto().getId() : null));


        if (item.getQuantidade() <= 0) {
            throw new NegocioException("A quantidade do produto precisa ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new NaoEncontradoException("Produto não encontrado"));

        estoqueService.validarEstoque(produto.getId(), pedido.getUnidade().getId(), item.getQuantidade());


        item.setProduto(produto);
        item.setValor(produto.getPreco());
        item.setPedido(pedido);

        pedido.getItens().add(item);
    }


    @Transactional
    public Pedido adicionarItem(UUID id, ItemPedido itemPedido) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new NegocioException("Pedido precisa estar em aberto para adicionar itens");
        }

        processarNovoItem(pedido, itemPedido);

        recalcular(pedido);

//        estoqueService.validarEstoque(produto.getId(), pedido.getUnidade().getId(), itemPedido.getQuantidade());
        return pedidoRepository.saveAndFlush(pedido);
    }


    private String getUsuarioLogado() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    @Transactional
    public Pedido removerItem(UUID pedidoId, UUID itemId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new NegocioException("Pedido precisa estar em aberto para remover itens");
        }

        pedido.getItens().removeIf(item -> item.getId().equals(itemId));

        recalcular(pedido);

        return pedidoRepository.save(pedido);
    }

    public Pedido cancelarPedido(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));


        if (pedido.getStatus() == StatusPedido.ENTREGUE || pedido.getStatus() == StatusPedido.RETIRADO || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new NegocioException("Não é possivel cancelar um pedido que já foi finalizado ou cancelado");
        }

        if (pedido.getPontosResgate() != null && pedido.getPontosResgate() > 0) {
            Cliente cliente = pedido.getCliente();
            //pega o saldo atual e soma os pontos que ele tinha gastado
            cliente.setSaldoPontos(cliente.getSaldoPontos() + pedido.getPontosResgate());
            //Opcional zera os pontos do pedido para não estornar duas vezes em caso de bug
            pedido.setPontosResgate(0);
            pedido.setDescontoFidelidade(BigDecimal.ZERO);

            clienteRepository.save(cliente);
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> buscarPedido(UUID id) {
        return pedidoRepository.findById(id);
    }


    private void recalcular(Pedido pedido) {
        BigDecimal subtotal = pedido.getItens().stream()
                .map(item -> item.getValor().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pedido.getCupom() != null) {
            subtotal = cupomService.aplicarDesconto(pedido.getCupom(), subtotal);
        }

        pedido.setTotal(subtotal);
    }

    @Transactional
    public Pedido aplicarCupom(UUID pedidoId, String codigo) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new NegocioException("Não é possível aplicar cupom neste pedido");
        }

        Cupom cupom = cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new CupomNaoEncontradoException("Cupom não encontrado"));

        pedido.setCupom(cupom);

        recalcular(pedido);

        return pedido;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorCanal(CanalPedido canal) {
        List<Pedido> pedidosDoBanco = pedidoRepository.findByCanalPedido(canal.name());

        return pedidosDoBanco.stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public List<Pedido> buscarPedidosClienteLogado() {
        String email = getUsuarioLogado();

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));

        return pedidoRepository.findByCliente(cliente);
    }

    public PedidoResponseDTO toDTO(Pedido pedido) {

        List<ItemResponseDTO> itens = pedido.getItens().stream()
                .map(item -> new ItemResponseDTO(
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getValor()
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCanalPedido(),
                pedido.getStatus().name(),
                pedido.getTotal(),
                pedido.getCupom() != null ? pedido.getCupom().getCodigo() : null,
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao(),
                itens
        );
    }

    @Transactional
    public Pedido preparar(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));


        if (pedido.getStatus() != StatusPedido.PAGO) {
            throw new NegocioException("Pedido precisa estar pago para ir para preparo");
        }

        pedido.setStatus(StatusPedido.EM_PREPARO);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido marcarComoPronto(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.EM_PREPARO) {
            throw new NegocioException("Pedido precisa estar em preparo");
        }

        pedido.setStatus(StatusPedido.PRONTO);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido finalizarPedido(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));


        if (pedido.getStatus() != StatusPedido.PRONTO) {
            throw new NegocioException("Pedido precisa estar pronto");
        }

        if (pedido.getCanalPedido().equals(CanalPedido.APP.name()) || pedido.getCanalPedido().equals(CanalPedido.WEB.name())) {
            pedido.setStatus(StatusPedido.ENTREGUE);
        } else {

            pedido.setStatus(StatusPedido.RETIRADO);
        }

        return pedidoRepository.save(pedido);
    }


    @Transactional(readOnly = true)
    public Pedido buscarPorId(UUID pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado com o ID: " + pedidoId));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarMeuCarrinho() {
        String email = getUsuarioLogado();

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("Cliente não encontrado"));
        Pedido carrinhoAberto = pedidoRepository.findByClienteIdAndStatus(cliente.getId(), StatusPedido.CRIADO)
                .orElseThrow(() -> new NaoEncontradoException("Você não possui nenhum carrinho em aberto no momento"));

        return toDTO(carrinhoAberto);
    }


}

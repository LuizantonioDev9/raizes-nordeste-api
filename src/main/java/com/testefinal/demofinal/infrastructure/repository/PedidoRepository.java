package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.enums.CanalPedido;
import com.testefinal.demofinal.domain.enums.StatusPedido;
import com.testefinal.demofinal.domain.model.Cliente;
import com.testefinal.demofinal.domain.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    Optional<Pedido> findByClienteIdAndStatus(Cliente cliente, StatusPedido status);

    @EntityGraph(attributePaths = {"itens","itens.produto","cupom", "cliente", "unidade"})
    List<Pedido> findByCliente (Cliente cliente);
    List<Pedido> findByCanalPedido(String canalPedido);
    Optional<Pedido> findByClienteIdAndStatus(UUID clienteId, StatusPedido status);
}

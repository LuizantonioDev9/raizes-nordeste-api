package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    Optional<Pagamento> findByPedidoId(UUID pedidoId);

}

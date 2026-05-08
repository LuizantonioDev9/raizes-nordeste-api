package com.testefinal.demofinal.domain.model;

import com.testefinal.demofinal.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String metodo;
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    private String payloadRetorno;
    private BigDecimal valor;
    private LocalDateTime dataDoPagamento;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
}

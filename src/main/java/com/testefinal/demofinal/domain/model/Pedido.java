package com.testefinal.demofinal.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.testefinal.demofinal.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "total")
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    private String canalPedido;
    @CreatedDate
    private LocalDateTime dataCriacao;
    @LastModifiedDate
    @JsonFormat(pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;
    @ManyToOne
    @JoinColumn(name = "id_cupom")
    private Cupom cupom;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    @Column(name = "descontoFidelidade")
    private BigDecimal descontoFidelidade;
    @Column(name = "pontosResgate")
    private Integer pontosResgate;


}

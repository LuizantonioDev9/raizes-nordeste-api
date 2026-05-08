package com.testefinal.demofinal.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "estoque")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

}

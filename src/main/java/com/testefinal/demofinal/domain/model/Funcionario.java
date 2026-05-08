package com.testefinal.demofinal.domain.model;

import com.testefinal.demofinal.domain.enums.FuncionarioCargo;
import com.testefinal.demofinal.domain.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private String telefone;
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;


}

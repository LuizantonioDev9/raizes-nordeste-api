package com.testefinal.demofinal.domain.model;

import com.testefinal.demofinal.domain.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    @Column(nullable = false)
    private String telefone;
    @Column(nullable = false)
    private String senha;
    @Enumerated(EnumType.STRING)
    private Perfil perfil;
    @Column(name = "saldo_pontos")
    private Integer saldoPontos =0;
    @Column(name = "participar_fidelidade")
    private Boolean participarFidelidade = false;
    private LocalDateTime dataConsentimentoFidelidade;
    @Column(name = "aceite_politica_privac", nullable = false)
    private Boolean aceitaPoliticaPrivacidade = false;
    @Column(name = "data_consentimento_privac")
    private LocalDateTime dataConsentimentoPrivacidade;


}

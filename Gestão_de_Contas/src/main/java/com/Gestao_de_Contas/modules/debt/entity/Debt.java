package com.Gestao_de_Contas.modules.debt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "debt")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String devedorNome;
    private String devedorEmail;
    private BigDecimal valor;
    private String descricao;

    private LocalDateTime dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusDivida status;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;
    private BigDecimal taxValue;

    private BigDecimal valorAtual;
    private LocalDateTime lembreteEnviado;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}

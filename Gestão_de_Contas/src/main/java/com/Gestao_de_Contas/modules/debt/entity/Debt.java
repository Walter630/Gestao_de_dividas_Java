package com.Gestao_de_Contas.modules.debt.entity;

import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.user.entity.User;
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

    private BigDecimal valorOriginal;
    private String descricao;

    private LocalDateTime dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusDivida status;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;
    private BigDecimal taxJuros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",  nullable = false)
    private Client clientId;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}

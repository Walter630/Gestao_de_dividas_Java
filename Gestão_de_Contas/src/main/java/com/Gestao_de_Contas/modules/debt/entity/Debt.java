package com.Gestao_de_Contas.modules.debt.entity;

import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.payment.entity.Payment;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Column(nullable = false)
    private PaymentMode taxType;
    private BigDecimal taxJuros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",  nullable = false)
    private Client clientId;
    private Integer numeroParcelas;
    @OneToMany(mappedBy = "debt",  fetch = FetchType.EAGER)
    private List<Payment> payments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}

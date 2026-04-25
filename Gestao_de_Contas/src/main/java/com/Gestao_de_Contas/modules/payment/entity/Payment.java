package com.Gestao_de_Contas.modules.payment.entity;

import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.useCase.DebtUseCase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    private BigDecimal taxValue;
    private BigDecimal valuePrincipal;
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "debt_id")
    private Debt debt;

    public void setDebt(DebtUseCase debtUseCase) {
    }
}

package com.Gestao_de_Contas.modules.subdescription.entity;

import com.Gestao_de_Contas.modules.plan.entity.Plan;
import com.Gestao_de_Contas.modules.plan.entity.PlanType;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private String mercadoPagoPaymentId; // ID do pagamento no MP
    private LocalDateTime activatedAt;
    private LocalDateTime expiresAt;     // 30 dias após ativação

    @CreationTimestamp
    private LocalDateTime createdAt;
}

package com.Gestao_de_Contas.modules.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PlanType type;

    private String name;
    private BigDecimal price;      // 0, 29.99, 49.99
    private Integer debtLimit;     // 5, 50, null (ilimitado)
    private Boolean advancedReports;
    private Boolean exclusiveFeatures;
}

package com.Gestao_de_Contas.modules.configurationSistem.entity;

import com.Gestao_de_Contas.modules.debt.entity.PaymentMode;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "configuration")
public class ConfigurationSistem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String nameEmployee;
    private BigDecimal taxPadrao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode typePayment;

    private String whatsappTemplate;
}

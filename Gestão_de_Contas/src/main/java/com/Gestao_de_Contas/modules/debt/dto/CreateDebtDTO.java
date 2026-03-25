package com.Gestao_de_Contas.modules.debt.dto;

import com.Gestao_de_Contas.modules.debt.dto.*;

import com.Gestao_de_Contas.modules.debt.entity.PaymentMode;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateDebtDTO {
        @NotNull
        @Positive
        private BigDecimal valorOriginal;

        @NotBlank
        private String descricao;

        @NotNull
        @Future  // data de vencimento tem que ser no futuro
        private LocalDateTime dataVencimento;

        @NotNull
        private PaymentMode taxType;

        @NotNull
        @DecimalMin("0.0") @DecimalMax("1.0") // 0% a 100%
        private BigDecimal taxJuros;

        private Integer numeroParcelas;

        private UUID userId;
}
package com.Gestao_de_Contas.modules.debt.dto;

import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.debt.entity.TaxType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DebtDTO(
        @NotBlank
        String dividorNome,
        @Email(message = "Email obrigatorio")
        String dividorEmail,
        BigDecimal valor,
        String descricao,
        @Future
        LocalDateTime dataVencimento,
        @NotNull
        StatusDivida status,
        @NotNull
        TaxType taxType,
        BigDecimal taxValue,
        LocalDateTime lembreteEnviado
) {
}

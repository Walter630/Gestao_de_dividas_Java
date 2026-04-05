package com.Gestao_de_Contas.modules.debt.dto;

import com.Gestao_de_Contas.modules.debt.entity.PaymentMode;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DebtResponseDTO(
        UUID id,
        String descricao,
        String devedorName,
        BigDecimal valorOriginal,
        StatusDivida status,
        LocalDateTime dataVencimento,
        PaymentMode taxType,
        BigDecimal taxJuros,
        Integer numeroParcelas,
        // ✅ só dados simples do client, sem trazer o objeto inteiro
        UUID clientId,
        String clientName,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {}
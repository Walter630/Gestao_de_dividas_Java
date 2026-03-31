package com.Gestao_de_Contas.modules.pix.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// ─────────────────────────────────────────────────────────────
// REQUEST
// ─────────────────────────────────────────────────────────────

/**
 * Dados necessários para gerar uma cobrança Pix.
 * O txid é gerado automaticamente se não for informado.
 */
public record PixDTO(

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        Double valor,

        String descricao,   // Aparece no app do pagador (max 25 chars)
        String txid         // ID único — se nulo, será gerado automaticamente
) {}


// ─────────────────────────────────────────────────────────────
// RESPONSE
// ─────────────────────────────────────────────────────────────


package com.Gestao_de_Contas.modules.cartaocredito.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record cartaoCreditoDTO(
        @NotNull
        String name,

        @NotNull
        Integer limit,

        @NotNull
        Integer limitDisponivel,

        @NotNull
        @Min(1)
        @Max(31)
        Integer diaFechamento,

        @NotNull
        @Min(1)
        @Max(31)
        Integer diaVencimento,

        Boolean ativo,
        UUID userId
) {
}
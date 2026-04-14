package com.Gestao_de_Contas.modules.compraparcelada.dto;

import com.Gestao_de_Contas.modules.cartaocredito.entity.CartCreditEntity;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CompraParceladaDTO(

        @NotNull(message = "Informe o cartão de crédito")
        @Positive(message = "Número do cartão inválido")
        CartCreditEntity cartaoCredito,

        @NotBlank(message = "Informe a loja")
        @Size(min = 2, max = 100, message = "Loja deve ter entre 2 e 100 caracteres")
        String loja,

        @Size(max = 255, message = "Descrição muito longa")
        String descricao,

        @NotNull(message = "Informe a data da compra")
        @PastOrPresent(message = "Data da compra não pode ser no futuro")
        LocalDateTime dataCompra,

        @NotNull(message = "Informe o valor total")
        @Positive(message = "Valor total deve ser maior que zero")
        Integer valorTotal,

        @NotNull(message = "Informe a quantidade de parcelas")
        @Min(value = 1, message = "Mínimo 1 parcela")
        @Max(value = 72, message = "Máximo 72 parcelas") // ~6 anos, padrão do mercado
        Integer quantidadeParcelas,

        @Size(max = 50, message = "Categoria muito longa")
        String categoria,

        @NotNull(message = "Informe se há juros")
        Boolean juros,

        @Min(value = 0, message = "Taxa de juros não pode ser negativa")
        @Max(value = 100, message = "Taxa de juros não pode passar de 100%")
        Integer taxaJuros,

        @NotNull(message = "Informe o status")
        StatusDivida status

) {}

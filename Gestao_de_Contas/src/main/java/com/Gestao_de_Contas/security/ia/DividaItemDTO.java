package com.Gestao_de_Contas.security.ia;

import java.math.BigDecimal;

public record DividaItemDTO(
        String descricao,
        Double valorTotal,
        BigDecimal juros,
        String vencimento
) {}

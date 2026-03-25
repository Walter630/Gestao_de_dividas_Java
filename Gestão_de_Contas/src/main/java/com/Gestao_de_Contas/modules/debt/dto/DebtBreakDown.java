package com.Gestao_de_Contas.modules.debt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data @AllArgsConstructor
public class DebtBreakDown {
    private BigDecimal valorOriginal;
    private BigDecimal jurosAcumulados;
    private BigDecimal totalJurosPagos;
    private BigDecimal totalPrincipalPago;
    private BigDecimal saldoPrincipal;
    private BigDecimal jurosPendentes;
}

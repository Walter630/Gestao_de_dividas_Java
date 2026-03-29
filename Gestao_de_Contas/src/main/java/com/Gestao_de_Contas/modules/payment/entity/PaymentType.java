package com.Gestao_de_Contas.modules.payment.entity;

public enum PaymentType {
    JUROS,    // Pagamento cobre comente o juros
    PARCELA,  //Pagamento como amortizacao (juros + principal)
    QUITACAO  // Pagamento para quitar o principal total mais o juros
}

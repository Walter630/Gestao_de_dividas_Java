package com.Gestao_de_Contas.modules.subdescription.entity;

public enum SubscriptionType {
    PENDING,    // aguardando pagamento
    ACTIVE,     // pago e ativo
    EXPIRED,    // venceu os 30 dias
    CANCELLED   // cancelado
}

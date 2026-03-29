package com.Gestao_de_Contas.modules.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtNotificationEventEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String emailId; // Email do usuario que ira receber
    private String clientName; // nome do cliente que vai receber
    private BigDecimal valorPendente;
    private LocalDateTime dataVencimento;
    private String tipo; // "ATRASADO", "PROXIMO_VENCIMENTO", "PAGAMENTO_RECEBIDO", "QUITADO"
}

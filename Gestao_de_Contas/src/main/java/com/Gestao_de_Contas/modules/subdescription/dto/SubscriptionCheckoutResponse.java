package com.Gestao_de_Contas.modules.subdescription.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCheckoutResponse {
    private String paymentId;
    private String qrCode;        // código copia e cola
    private String qrCodeBase64;  // imagem do QR Code
    private String planName;
    private BigDecimal price;
    private Boolean activated;    // true para plano gratuito
}
package com.Gestao_de_Contas.modules.pix.service;

import com.Gestao_de_Contas.modules.pix.dto.PixCobrancaResponseDTO;
import com.Gestao_de_Contas.modules.pix.entity.PlanType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


// PlanoPixService.java
@Service
public class PixPlanService {

    private final pixService pixService;
    private final GenerateQRCodeService qrCodeService;

    @Value("${pix.chave}")
    private String chave;

    public PixPlanService(pixService pixService, GenerateQRCodeService qrCodeService) {
        this.pixService = pixService;
        this.qrCodeService = qrCodeService;
    }

    public PixCobrancaResponseDTO gerarCobrancaPlano(PlanType plano) {

        if (plano.getValor() == 0.0) {
            throw new RuntimeException("Plano Free não requer pagamento.");
        }

        // ✅ Valor vem do Enum — ninguém de fora define isso
        String payload = pixService.gerarPayload(
                plano.getValor(),
                "Assinatura " + plano.getNome(),
                plano.getTxid()
        );

        String qrCode = qrCodeService.gerarQRCodeBase64(payload);

        return new PixCobrancaResponseDTO(
                plano.getTxid(),           // ou plano.getTxid()
                plano.getValor(),
                "Assinatura " + plano.getNome(),
                payload,
                qrCode,
                chave   // ou injetar via @Value
        );
    }
}

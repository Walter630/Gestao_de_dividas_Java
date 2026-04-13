package com.Gestao_de_Contas.modules.pix.controller;


import com.Gestao_de_Contas.modules.pix.dto.PixCobrancaResponseDTO;
import com.Gestao_de_Contas.modules.pix.dto.PixDTO;
import com.Gestao_de_Contas.modules.pix.entity.PlanType;
import com.Gestao_de_Contas.modules.pix.service.GenerateQRCodeService;
import com.Gestao_de_Contas.modules.pix.service.PixPlanService;
import com.Gestao_de_Contas.modules.pix.service.pixService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class pixController {

    private final pixService pixEMVService;
    private final GenerateQRCodeService qrCodeService;
    private final PixPlanService  pixPlanService;

    @Value("${pix.chave}")
    private String chavePix;

    public pixController(pixService pixEMVService, GenerateQRCodeService qrCodeService,  PixPlanService pixPlanService) {
        this.pixEMVService = pixEMVService;
        this.qrCodeService = qrCodeService;
        this.pixPlanService = pixPlanService;
    }

    /**
     * Gera uma cobrança Pix com QR Code e copia-e-cola.
     *
     * POST /api/pix/cobranca
     * {
     *   "valor": 150.00,
     *   "descricao": "Parcela divida #3",
     *   "txid": "divida123parcela3"   ← opcional
     * }
     */
    @PostMapping("/cobranca")
    public ResponseEntity<PixCobrancaResponseDTO> gerarCobranca(
            @Valid @RequestBody PixDTO request) {

        // Gera txid automaticamente se não for informado
        String txid = (request.txid() != null && !request.txid().isBlank())
                ? request.txid()
                : gerarTxid();

        String descricao = (request.descricao() != null && !request.descricao().isBlank())
                ? request.descricao()
                : "Cobranca";

        // 1. Gera o payload EMV (copia e cola)
        String payload = pixEMVService.gerarPayload(request.valor(), descricao, txid);

        // 2. Gera a imagem do QR Code em Base64
        String qrCodeBase64 = qrCodeService.gerarQRCodeBase64(payload);

        // 3. Monta e retorna a resposta
        PixCobrancaResponseDTO response = new PixCobrancaResponseDTO(
                txid,
                request.valor(),
                descricao,
                payload,
                qrCodeBase64,
                chavePix
        );

        return ResponseEntity.ok(response);
    }

    /** Gera um txid único baseado em UUID (25 chars, apenas alfanumérico) */
    private String gerarTxid() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 25);
    }

    @PostMapping("/pagamento/{plano}")
    public ResponseEntity<PixCobrancaResponseDTO> gerarPagamento(
            @PathVariable String plano
    ) {
        try {
            PlanType planType = PlanType.valueOf(plano.toUpperCase());
            var result = this.pixPlanService.gerarCobrancaPlano(planType);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
package com.Gestao_de_Contas.modules.pix.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Gera a imagem do QR Code a partir do payload Pix EMV.
 * Retorna Base64 pronto para usar no frontend ou no e-mail.
 */
@Service
public class GenerateQRCodeService {

    private static final int TAMANHO_PADRAO = 300; // pixels

    /**
     * Gera a imagem do QR Code e retorna em Base64 com prefixo data:image.
     *
     * @param payload O texto do copia-e-cola Pix (payload EMV)
     * @param tamanho Tamanho da imagem em pixels (ex: 300)
     * @return String no formato "data:image/png;base64,..."
     */
    public String gerarQRCodeBase64(String payload, int tamanho) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // margem mínima (quiet zone)

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(payload, BarcodeFormat.QR_CODE, tamanho, tamanho, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            byte[] pngBytes = outputStream.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(pngBytes);

            return "data:image/png;base64," + base64;

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code Pix: " + e.getMessage(), e);
        }
    }

    /** Versão com tamanho padrão (300px) */
    public String gerarQRCodeBase64(String payload) {
        return gerarQRCodeBase64(payload, TAMANHO_PADRAO);
    }
}
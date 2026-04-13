package com.Gestao_de_Contas.modules.pix.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class pixService {

    @Value("${pix.chave}")
    private String chavePix;

    @Value("${pix.nome-beneficiario}")
    private String nomeBeneficiario;

    @Value("${pix.cidade}")
    private String cidade;

    /**
     * Gera o payload EMV completo com CRC16 ao final.
     *
     * @param valor     Valor da cobrança (ex: 150.00)
     * @param descricao Descrição curta (max 25 chars)
     * @param txid      ID único da transação (max 25 chars, sem espaços)
     * @return String payload — o famoso "copia e cola" do Pix
     */
    public String gerarPayload(Double valor, String descricao, String txid) {
        // Sanitiza inputs
        String descricaoSanitizada = sanitizar(descricao, 25);
        String txidSanitizado      = sanitizarTxid(txid);
        String nomeSanitizado      = sanitizar(nomeBeneficiario, 25);
        String cidadeSanitizada    = sanitizar(cidade, 15);

        // ── ID 26: Merchant Account Information ──────────────────────────
        String gui        = buildField("00", "br.gov.bcb.pix");
        String chaveField = buildField("01", chavePix);
        String infoField  = buildField("02", descricaoSanitizada);
        String merchantAccount = buildField("26", gui + chaveField + infoField);

        // ── ID 62: Additional Data Field (txid) ──────────────────────────
        String txidField      = buildField("05", txidSanitizado);
        String additionalData = buildField("62", txidField);

        // ── Monta payload sem CRC ─────────────────────────────────────────
        StringBuilder sb = new StringBuilder();
        sb.append(buildField("00", "01"));               // Payload Format Indicator
        sb.append(buildField("01", "11"));               // Point of Initiation Method (12 = dinâmico/único uso)
        sb.append(merchantAccount);                       // ID 26
        sb.append(buildField("52", "0000"));             // Merchant Category Code
        sb.append(buildField("53", "986"));              // Transaction Currency (986 = BRL)

        if (valor != null && valor > 0) {
            sb.append(buildField("54", formatarValor(valor))); // ID 54: valor
        }

        sb.append(buildField("58", "BR"));               // Country Code
        sb.append(buildField("59", nomeSanitizado));     // Merchant Name
        sb.append(buildField("60", cidadeSanitizada));   // Merchant City
        sb.append(additionalData);                        // ID 62

        // ── ID 63: CRC16 (sempre ao final) ───────────────────────────────
        // Append "6304" antes de calcular — faz parte da spec
        String payloadSemCRC = sb.toString() + "6304";
        String crc = calcularCRC16(payloadSemCRC);

        return payloadSemCRC + crc;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────────────────────────────

    /** Formata campo no padrão ID + tamanho (2 dígitos) + valor */
    private String buildField(String id, String value) {
        return id + String.format("%02d", value.length()) + value;
    }

    /** Formata o valor monetário com 2 casas decimais, sem ponto para milhar */
    private String formatarValor(Double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    /** Remove caracteres especiais e limita tamanho */
    private String sanitizar(String input, int maxLength) {
        if (input == null) return "";
        String clean = input.replaceAll("[^a-zA-Z0-9 @:./-]", "")
                .trim();
        return clean.length() > maxLength ? clean.substring(0, maxLength) : clean;
    }

    /** Txid: apenas letras e números, max 25 chars */
    private String sanitizarTxid(String txid) {
        if (txid == null) return "***";
        String clean = txid.replaceAll("[^a-zA-Z0-9]", "");
        clean = clean.isEmpty() ? "pagamento" : clean;
        return clean.length() > 25 ? clean.substring(0, 25) : clean;
    }

    /**
     * CRC-16/CCITT-FALSE
     * Polinômio: 0x1021 | Valor inicial: 0xFFFF
     * Retorna 4 caracteres hexadecimais em maiúsculas.
     */
    private String calcularCRC16(String payload) {
        int crc = 0xFFFF;
        byte[] bytes = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }

        return String.format("%04X", crc);
    }
}
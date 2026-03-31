package com.Gestao_de_Contas.modules.pix.dto;

/**
 * Resposta com o QR Code e o copia-e-cola do Pix.
 */

public record PixCobrancaResponseDTO(
        String txid,            // ID da transação gerado
        Double valor,           // Valor da cobrança
        String descricao,       // Descrição informada
        String payload,         // Copia e cola — o texto do Pix
        String qrCodeBase64,    // Imagem PNG em Base64 — "data:image/png;base64,..."
        String chavePix         // Chave Pix usada (para exibir ao usuário)
) {}

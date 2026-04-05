package com.Gestao_de_Contas.modules.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String name,
        String email,
        String telefone,
        String cpf,
        LocalDateTime createAt
) {
}

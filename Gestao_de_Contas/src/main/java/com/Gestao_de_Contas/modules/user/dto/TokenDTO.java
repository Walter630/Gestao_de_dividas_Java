package com.Gestao_de_Contas.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String refreshToken;
    private String userId;
    private String role;
}

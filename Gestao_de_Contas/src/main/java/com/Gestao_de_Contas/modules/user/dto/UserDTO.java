package com.Gestao_de_Contas.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "Nome nao pode ser nulo")
        String username,
        @NotNull
        @Email
        String email,
        @NotNull
        @Size(min = 1, max = 30)
        String password
) {
}

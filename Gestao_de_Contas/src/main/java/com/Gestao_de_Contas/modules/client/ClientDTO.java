package com.Gestao_de_Contas.modules.client;


import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ClientDTO {
    @NotNull(message = "Nome de usuario nao pode ser vazio")
    private String name;
    @Email
    private String email;
    private String telefone;
    private String cpf;
    private UUID userId;
}

package com.Gestao_de_Contas.modules.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientNotificationEventEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String emailUser;
    private String nameClient;
    private String telefoneClient;
}

package com.Gestao_de_Contas.modules.client.controller;

import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.client.repository.ClientRepository;
import com.Gestao_de_Contas.modules.client.useCase.ClientUseCase;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientUseCase clientUseCase;

    @GetMapping
    public List<Client> findAll() {
        return clientUseCase.findAll();
    }

    @PostMapping
    public Client save(@Valid @RequestBody Client client, @AuthenticationPrincipal User user) {
        System.out.println("Usuário logado: " + user); // ← adiciona isso
        client.setUserId(user);
        return clientUseCase.create(client);
    }
}

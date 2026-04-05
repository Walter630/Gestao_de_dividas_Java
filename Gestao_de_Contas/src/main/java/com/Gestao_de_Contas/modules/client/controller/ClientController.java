package com.Gestao_de_Contas.modules.client.controller;

import com.Gestao_de_Contas.modules.client.dto.ClientDTO;
import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.client.useCase.ClientUseCase;
import com.Gestao_de_Contas.modules.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<?> save(@Valid @RequestBody ClientDTO client, @AuthenticationPrincipal User user) {
        try {
            System.out.println("Recebendo cliente: " + client.getName() + " | Email: " + client.getEmail());
            System.out.println("User logado: " + user.getEmail() + " | ID: " + user.getId());

            client.setUserId(user.getId());
            var created = clientUseCase.create(client, user);
            System.out.println("Cliente criado com sucesso: " + created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public Client update(@Valid @RequestBody Client client, @AuthenticationPrincipal User user) {
        return clientUseCase.update(client, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        this.clientUseCase.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return clientUseCase.findById(id);
    }
}

package com.Gestao_de_Contas.modules.client.useCase;

import com.Gestao_de_Contas.modules.client.ClientDTO;
import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.client.entity.ClientNotificationEventEntity;
import com.Gestao_de_Contas.modules.client.repository.ClientRepository;
import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.security.RabbitMQConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientUseCase {
    private final ClientRepository clientRepository;
    private final RabbitTemplate rabbitTemplate;

    public Client create(@Valid ClientDTO client, User userLogado) {
        //valido se o email é nulo ou se existe um email preeenchido
        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            clientRepository.findByEmail(client.getEmail()).ifPresent(c -> {
                throw new RuntimeException("email ja cadastrado");
            });
        }
        Client clientCreate = Client.builder()
                .name(client.getName())
                .email(client.getEmail())
                .cpf(client.getCpf())
                .telefone(client.getTelefone())
                .userId(userLogado)
                .build();

        Client savedClient = clientRepository.save(clientCreate);

        //envia a notificação
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CLIENTE_ROUTING,
                new ClientNotificationEventEntity(
                        userLogado.getEmail(),
                        client.getName(),
                        savedClient.getTelefone()
                )
        );
        return savedClient;
    }

    public Client update(Client client, @AuthenticationPrincipal User user) {
        clientRepository.findById(client.getId()).ifPresent(client1 -> {throw new RuntimeException("nome do cliente existente");});
        return clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(UUID id) {
        if (clientRepository.findById(id).isPresent()) {
            return clientRepository.findById(id).get();
        }
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("cliente nao encontrado"));
    }

    public void delete(UUID clientId, UUID userId) {

        // Busca o cliente e verifica se ele pertence ao usuário
        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (!client.getUserId().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para deletar este cliente");
        }
        clientRepository.delete(client);
    }
}

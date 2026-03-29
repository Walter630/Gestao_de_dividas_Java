package com.Gestao_de_Contas.modules.client.useCase;

import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.client.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientUseCase {
    private final ClientRepository clientRepository;
    public ClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(UUID id) {
        return clientRepository.findById(id).get();
    }
}

package com.Gestao_de_Contas.modules.client.repository;

import com.Gestao_de_Contas.modules.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findById(UUID id);
}

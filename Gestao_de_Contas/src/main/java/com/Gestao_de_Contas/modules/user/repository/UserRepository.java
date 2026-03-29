package com.Gestao_de_Contas.modules.user.repository;

import com.Gestao_de_Contas.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    Optional<User> findById(UUID uuid);
    Optional<User> findByUsername(String username);
}

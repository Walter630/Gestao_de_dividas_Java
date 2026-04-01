package com.Gestao_de_Contas.modules.debt.repository;

import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DebtRepository extends JpaRepository<Debt, UUID> {
    Optional<Debt> findById(UUID uuid);
    // Busca todas as dívidas de um devedor específico
    List<Debt> findByDevedorNameContainingIgnoreCase(String devedorName);

    // Busca dívidas que vencem hoje (útil para o seu sistema de alerta)
    List<Debt> findByDataVencimento(LocalDate date);

    // Busca apenas as dívidas que ainda não foram pagas
    List<Debt> findByStatus(StatusDivida status);
    List<Debt> findAllByUser(User user);
    long countByUser(@Param("userId") UUID userId);
}

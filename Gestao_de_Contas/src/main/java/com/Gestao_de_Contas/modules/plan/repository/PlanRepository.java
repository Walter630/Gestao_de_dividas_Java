package com.Gestao_de_Contas.modules.plan.repository;

import com.Gestao_de_Contas.modules.plan.entity.Plan;
import com.Gestao_de_Contas.modules.plan.entity.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByType(PlanType type);
}

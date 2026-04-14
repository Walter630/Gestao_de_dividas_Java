package com.Gestao_de_Contas.modules.compraparcelada.repository;

import com.Gestao_de_Contas.modules.compraparcelada.entity.CompraParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface compraParceladaRepository extends JpaRepository<CompraParcelEntity, UUID> {
    // Implemente sua lógica aqui, Walter!
}

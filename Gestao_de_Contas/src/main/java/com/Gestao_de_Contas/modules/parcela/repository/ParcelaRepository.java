package com.Gestao_de_Contas.modules.parcela.repository;

import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParcelaRepository extends JpaRepository<ParcelEntity, UUID> {

}

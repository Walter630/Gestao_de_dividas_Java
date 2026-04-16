package com.Gestao_de_Contas.modules.parcela.repository;

import com.Gestao_de_Contas.modules.parcela.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParcelaRepository extends JpaRepository<ParcelEntity, UUID> {
    List<ParcelEntity> findByCompraId_CartaoCredito_Id(UUID cartaoId);

    @Query("SELECT p FROM ParcelEntity p WHERE MONTH(p.dataVencimento) = :month AND YEAR(p.dataVencimento) = :year")
    List<ParcelEntity> findByDataVencimentoMonthAndDataVencimentoYear(@Param("month") int month, @Param("year") int year);
}

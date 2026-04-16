package com.Gestao_de_Contas.modules.cartaocredito.repository;

import com.Gestao_de_Contas.modules.cartaocredito.entity.CartCreditEntity;
import com.Gestao_de_Contas.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartCreditEntity, UUID> {
    Optional<CartCreditEntity> findByName(String name);
    List<CartCreditEntity> findByUser(User userId);
}

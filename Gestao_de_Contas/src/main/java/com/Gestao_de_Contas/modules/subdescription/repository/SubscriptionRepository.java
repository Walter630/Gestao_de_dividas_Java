package com.Gestao_de_Contas.modules.subdescription.repository;

import com.Gestao_de_Contas.modules.subdescription.entity.Subscription;
import com.Gestao_de_Contas.modules.subdescription.entity.SubscriptionType;
import com.Gestao_de_Contas.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserAndType(User user, SubscriptionType type);
    Optional<Subscription> findByMercadoPagoPaymentId(String paymentId);
}

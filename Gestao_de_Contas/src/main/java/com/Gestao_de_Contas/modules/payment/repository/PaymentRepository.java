package com.Gestao_de_Contas.modules.payment.repository;

import com.Gestao_de_Contas.modules.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}

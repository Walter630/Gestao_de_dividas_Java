package com.Gestao_de_Contas.modules.payment.controller;

import com.Gestao_de_Contas.modules.debt.useCase.DebtUseCase;
import com.Gestao_de_Contas.modules.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/debts/{debtId}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final DebtUseCase debtUseCase;

    // POST /debts/{debtId}/payments → registra um pagamento
    @PostMapping
    public ResponseEntity<Payment> addPayment(
            @PathVariable UUID debtId,
            @RequestBody Payment payment) {
        return ResponseEntity.ok(debtUseCase.addPayment(debtId, payment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Payment> deletePayment(@PathVariable UUID debtId) {
        this.debtUseCase.delete(debtId);
        return ResponseEntity.noContent().build();
    }
}
package com.Gestao_de_Contas.security;

import com.Gestao_de_Contas.modules.debt.repository.DebtRepository;
import com.Gestao_de_Contas.modules.subdescription.service.SubscriptionService;
import com.Gestao_de_Contas.modules.subdescription.service.SubscriptionService;
import com.Gestao_de_Contas.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanGuard {

    private final SubscriptionService subscriptionService;
    private final DebtRepository debtRepository;

    public void checkDebtLimit(User user) {
        long count = debtRepository.countByUser(user.getId());

        if (!subscriptionService.canCreateDebt(user, count)) {
            throw new RuntimeException(
                    "Limite de dívidas atingido. Faça upgrade do seu plano."
            );
        }
    }
}
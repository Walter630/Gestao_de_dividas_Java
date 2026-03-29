package com.Gestao_de_Contas.modules.plan.entity;

import com.Gestao_de_Contas.modules.plan.entity.Plan;
import com.Gestao_de_Contas.modules.plan.entity.PlanType;
import com.Gestao_de_Contas.modules.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PlanRepository planRepository;

    @Override
    public void run(String... args) {
        // só cria se não existir
        if (planRepository.count() == 0) {
            planRepository.saveAll(List.of(
                    Plan.builder()
                            .type(PlanType.FREE)
                            .name("Gratuito")
                            .price(BigDecimal.ZERO)
                            .debtLimit(5)
                            .advancedReports(false)
                            .exclusiveFeatures(false)
                            .build(),
                    Plan.builder()
                            .type(PlanType.PRO)
                            .name("Profissional")
                            .price(new BigDecimal("29.99"))
                            .debtLimit(50)
                            .advancedReports(true)
                            .exclusiveFeatures(false)
                            .build(),
                    Plan.builder()
                            .type(PlanType.PREMIUM)
                            .name("Empresarial")
                            .price(new BigDecimal("49.99"))
                            .debtLimit(Integer.MAX_VALUE)
                            .advancedReports(true)
                            .exclusiveFeatures(true)
                            .build()
            ));
            System.out.println("✅ Planos criados com sucesso!");
        }
    }
}
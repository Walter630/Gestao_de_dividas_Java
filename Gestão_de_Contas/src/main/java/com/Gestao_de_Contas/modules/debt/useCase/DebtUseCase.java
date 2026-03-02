package com.Gestao_de_Contas.modules.debt.useCase;

import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.repository.DebtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtUseCase {

    private final DebtRepository debtRepository;

    @Transactional
    public Debt createDebt(Debt debtDTO) {
        return debtRepository.save(debtDTO);
    }

    public BigDecimal calculeCurrenteValue(Debt debt) {
        if (debt.getDataVencimento().isAfter(LocalDateTime.now())) {
            return debt.getValor();
        }
        else if (debt.getDataVencimento().isBefore(LocalDateTime.now())) {
            debt.setValorAtual(debt.getValorAtual().multiply(debt.getTaxValue()));
            return debt.getValorAtual();
        }

        debt.setValorAtual(debt.getValor());

        return switch (debt.getTaxType()) {
            case FIXED -> calculeFixedValue(debt);
            case PROGRESSIVE -> calculateProgressiveTax(debt);
            default -> debt.getValor();
        };
    }

    private BigDecimal calculeFixedValue(Debt debt) {
        BigDecimal taxAmount = debt.getValor().multiply(debt.getTaxValue());
        debt.setValorAtual(debt.getValor().add(taxAmount));
        return debt.getValorAtual();
    }

    private BigDecimal calculateProgressiveTax(Debt debt) {
        long daysOverdue = java.time.Duration.between(debt.getDataVencimento(), LocalDateTime.now()).toDays();

        // Regra: 5% base + 1% por dia, limitado a 50%
        BigDecimal rate = BigDecimal.valueOf(0.05)
                .add(BigDecimal.valueOf(daysOverdue * 0.01))
                .min(BigDecimal.valueOf(0.50));

        return debt.getValor().add(debt.getValor().multiply(rate));
    }

    public List<Debt> getAll() {
        List<Debt> debts = debtRepository.findAll();
        for (Debt debt : debts) {
            calculeCurrenteValue(debt);
        }
        return debtRepository.findAll();
    }

}

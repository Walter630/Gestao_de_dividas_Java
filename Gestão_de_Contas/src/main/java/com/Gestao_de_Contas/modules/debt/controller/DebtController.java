package com.Gestao_de_Contas.modules.debt.controller;

import com.Gestao_de_Contas.modules.debt.dto.DebtDTO;
import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.useCase.DebtUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/debt")
@RestController
@RequiredArgsConstructor
public class DebtController {
    private final DebtUseCase debtUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> createDebt(@Valid @RequestBody DebtDTO data) {
        try {
            var debtEntity = Debt.builder()
                    .devedorNome(data.dividorNome())
                    .devedorEmail(data.dividorEmail())
                    .valor(data.valor())
                    .descricao(data.descricao())
                    .dataVencimento(data.dataVencimento())
                    .status(data.status())
                    .taxType(data.taxType())
                    .taxValue(data.taxValue())
                    .lembreteEnviado(data.lembreteEnviado())
                    .build();
            var result = this.debtUseCase.createDebt(debtEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Debt>> getDividas() {
        try {
            var result = debtUseCase.getAll();
            for (Debt debt : result) {
                debt.setValorAtual(debtUseCase.calculeCurrenteValue(debt));
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

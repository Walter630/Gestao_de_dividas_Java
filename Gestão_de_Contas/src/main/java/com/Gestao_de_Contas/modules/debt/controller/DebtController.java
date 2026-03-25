package com.Gestao_de_Contas.modules.debt.controller;

import com.Gestao_de_Contas.modules.debt.dto.CreateDebtDTO;
import com.Gestao_de_Contas.modules.debt.dto.DebtBreakDown;
import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.useCase.DebtUseCase;
import com.Gestao_de_Contas.modules.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
@Tag(name = "Debt", description = "Crud de dividas")
public class DebtController {

    private final DebtUseCase debtUseCase;

    @Operation(summary = "Cadastro de dividas")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credentials invalid")
    })
    // POST /debts → cria uma dívida
    @PostMapping
    public ResponseEntity<Debt> createDebt(@Valid @RequestBody CreateDebtDTO debtdto, @AuthenticationPrincipal User userLogado) {
        try{
            debtdto.setUserId(userLogado.getId());
            return ResponseEntity.ok(debtUseCase.createDebt(debtdto));
        } catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(summary = "Get dividas por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nada encontrado")
    })
    // GET /debts/{id} → busca dívida por ID
    @GetMapping("/{id}")
    public ResponseEntity<Debt> getDebt(@PathVariable UUID id, @AuthenticationPrincipal User userLogado) {
        Debt debt = debtUseCase.getDebtIdByUser(id, userLogado.getId());
        if (!debt.getUserId().getId().equals(userLogado.getId())) {
            return ResponseEntity.status(403).build(); // usuário não é dono da dívida
        }
        return ResponseEntity.ok(debtUseCase.getDebtId(id));
    }

    // GET /debts/{id}/breakdown → retorna o cálculo completo
    @GetMapping("/{id}/breakdown")
    public ResponseEntity<DebtBreakDown> getBreakdown(@PathVariable UUID id, @AuthenticationPrincipal User userLogado) {
        Debt debt = debtUseCase.getDebtIdByUser(id, userLogado.getId());
        if (!debt.getUserId().getId().equals(userLogado.getId())) {
            return ResponseEntity.status(403).build(); // usuário não é dono da dívida
        }
        return ResponseEntity.ok(debtUseCase.calcJurosMensal(debt));
    }

    // GET /debts/{id}/proxima-parcela → valor da próxima parcela
    @GetMapping("/{id}/proxima-parcela")
    public ResponseEntity<BigDecimal> getProximaParcela(@PathVariable UUID id) {
        Debt debt = debtUseCase.getDebtId(id);
        return ResponseEntity.ok(debtUseCase.calcularProximaParcela(debt));
    }

    // PUT /debts/{id}/status → atualiza o status da dívida
    @PutMapping("/{id}/status")
    public ResponseEntity<Debt> atualizarStatus(@PathVariable UUID id) {
        Debt debt = debtUseCase.getDebtId(id);
        return ResponseEntity.ok(debtUseCase.updateStatus(debt));
    }

    @GetMapping()
    public ResponseEntity<List<Debt>> findMyDebts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(debtUseCase.getMyDebts(user.getId()));
    }
}
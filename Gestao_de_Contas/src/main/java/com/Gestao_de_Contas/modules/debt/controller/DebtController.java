package com.Gestao_de_Contas.modules.debt.controller;

import com.Gestao_de_Contas.modules.debt.dto.CreateDebtDTO;
import com.Gestao_de_Contas.modules.debt.dto.DebtBreakDown;
import com.Gestao_de_Contas.modules.debt.dto.DebtResponseDTO;
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
    public ResponseEntity<?> createDebt(@Valid @RequestBody CreateDebtDTO debtdto,
                                        @AuthenticationPrincipal User userLogado) {
        try {
            debtdto.setUserId(userLogado.getId());
            return ResponseEntity.ok(debtUseCase.createDebt(debtdto, userLogado)); // useCase já retorna DTO
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebtResponseDTO> getDebt(@PathVariable UUID id,
                                                   @AuthenticationPrincipal User userLogado) {
        // ✅ getDebtIdByUser já faz a verificação de dono — checagem dupla removida
        return ResponseEntity.ok(debtUseCase.getDebtByUser(id, userLogado.getId()));
    }

    @GetMapping("/{id}/breakdown")
    public ResponseEntity<DebtBreakDown> getBreakdown(@PathVariable UUID id,
                                                      @AuthenticationPrincipal User userLogado) {
        // breakdown usa Debt internamente — ok, nunca chega ao Jackson como entidade
        Debt debt = debtUseCase.getDebtIdByUser(id, userLogado.getId());
        return ResponseEntity.ok(debtUseCase.calcJurosMensal(debt));
    }

    @GetMapping("/{id}/proxima-parcela")
    public ResponseEntity<BigDecimal> getProximaParcela(@PathVariable UUID id,
                                                        @AuthenticationPrincipal User userLogado) {
        Debt debt = debtUseCase.getDebtIdByUser(id, userLogado.getId()); // ✅ garante que é dono
        return ResponseEntity.ok(debtUseCase.calcularProximaParcela(debt));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DebtResponseDTO> atualizarStatus(@PathVariable UUID id,
                                                           @AuthenticationPrincipal User userLogado) {
        Debt debt = debtUseCase.getDebtIdByUser(id, userLogado.getId()); // ✅ garante que é dono
        return ResponseEntity.ok(debtUseCase.updateStatusDTO(debt)); // ← novo método no useCase
    }

    @GetMapping
    public ResponseEntity<List<DebtResponseDTO>> findMyDebts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(debtUseCase.listarTodas(user)); // ✅ já retorna DTO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DebtResponseDTO> deleteDebt(@PathVariable UUID id) {
        this.debtUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

// /getAll é redundante com GET / — pode remover depois
}
package com.Gestao_de_Contas.modules.subdescription.controller;

import com.Gestao_de_Contas.modules.subdescription.dto.SubscriptionCheckoutResponse;
import com.Gestao_de_Contas.modules.plan.entity.PlanType;
import com.Gestao_de_Contas.modules.subdescription.service.SubscriptionService;
import com.Gestao_de_Contas.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // (1) usuário escolhe um plano e recebe o QR Code
    @PostMapping("/checkout/{planType}")
    public ResponseEntity<?> checkout(
            @PathVariable PlanType planType,
            @AuthenticationPrincipal User user) {
        try {
            SubscriptionCheckoutResponse response =
                    subscriptionService.checkout(planType, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // (2) webhook chamado pelo Mercado Pago após pagamento
    // NÃO tem autenticação — o MP chama direto
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload,
                                        @RequestHeader(value = "x-signature", required = false)
                                        String signature) {
        try {
            String type = (String) payload.get("type");
            // (3) extrai o ID do pagamento do payload
            if ("payment".equals(type) || payload.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String paymentId = data.get("id").toString();

                System.out.println("Webhook recebido — paymentId: " + paymentId);
                subscriptionService.confirmPayment(paymentId);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Erro no webhook: " + e.getMessage());
            return ResponseEntity.ok().build(); // ← sempre retorna 200 para o MP
        }
    }

    // (4) retorna o plano ativo do usuário logado
    @GetMapping("/my-plan")
    public ResponseEntity<?> myPlan(@AuthenticationPrincipal User user) {
        var subscription = subscriptionService.getActiveSubscription(user);
        if (subscription == null) {
            return ResponseEntity.ok(Map.of("plan", "FREE", "active", false));
        }
        return ResponseEntity.ok(Map.of(
                "plan", subscription.getPlan().getType(),
                "planName", subscription.getPlan().getName(),
                "active", true,
                "expiresAt", subscription.getExpiresAt()
        ));
    }
}
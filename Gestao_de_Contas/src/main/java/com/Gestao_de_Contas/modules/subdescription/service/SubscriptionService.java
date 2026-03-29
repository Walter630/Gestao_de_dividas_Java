package com.Gestao_de_Contas.modules.subdescription.service;

import com.Gestao_de_Contas.modules.plan.entity.Plan;
import com.Gestao_de_Contas.modules.plan.entity.PlanType;
import com.Gestao_de_Contas.modules.plan.repository.PlanRepository;
import com.Gestao_de_Contas.modules.subdescription.dto.SubscriptionCheckoutResponse;
import com.Gestao_de_Contas.modules.subdescription.entity.Subscription;
import com.Gestao_de_Contas.modules.subdescription.entity.SubscriptionType;
import com.Gestao_de_Contas.modules.subdescription.repository.SubscriptionRepository;
import com.Gestao_de_Contas.modules.subdescription.entity.*;
import com.Gestao_de_Contas.modules.subdescription.repository.*;
import com.Gestao_de_Contas.modules.user.entity.User;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Value("${mp.access-token}")
    private String accessToken;

    @Value("${mp.notification-url}")
    private String notificationUrl;

    // (1) cria a ordem de pagamento no Mercado Pago
    @Transactional
    public SubscriptionCheckoutResponse checkout(PlanType planType, User user) throws Exception {

        // busca o plano
        Plan plan = planRepository.findByType(planType)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        // plano gratuito não precisa de pagamento
        if (plan.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            return activateFree(plan, user);
        }

        // configura o SDK do Mercado Pago
        MercadoPagoConfig.setAccessToken(accessToken);

        // monta a requisição de pagamento Pix
        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(plan.getPrice())
                .description("Assinatura " + plan.getName() + " - Gestao de Contas")
                .paymentMethodId("pix")  // ← define como Pix
                .notificationUrl(notificationUrl)
                .payer(PaymentPayerRequest.builder()
                        .email(user.getUsername()) // ← usa o username como email por enquanto
                        .build())
                .build();

        // cria o pagamento no Mercado Pago
        PaymentClient client = new PaymentClient();
        Payment payment = client.create(request);

        // salva a subscription como PENDING
        subscriptionRepository.save(
                Subscription.builder()
                        .user(user)
                        .plan(plan)
                        .type(SubscriptionType.PENDING)
                        .mercadoPagoPaymentId(payment.getId().toString())
                        .build()
        );

        // retorna o QR Code para o frontend exibir
        return SubscriptionCheckoutResponse.builder()
                .paymentId(payment.getId().toString())
                .qrCode(payment.getPointOfInteraction()
                        .getTransactionData().getQrCode())
                .qrCodeBase64(payment.getPointOfInteraction()
                        .getTransactionData().getQrCodeBase64())
                .planName(plan.getName())
                .price(plan.getPrice())
                .build();
    }

    // (2) ativa o plano gratuito direto sem pagamento
    private SubscriptionCheckoutResponse activateFree(Plan plan, User user) {
        subscriptionRepository.save(
                Subscription.builder()
                        .user(user)
                        .plan(plan)
                        .type(SubscriptionType.ACTIVE)
                        .activatedAt(LocalDateTime.now())
                        .expiresAt(null) // gratuito não expira
                        .build()
        );
        return SubscriptionCheckoutResponse.builder()
                .planName(plan.getName())
                .price(BigDecimal.ZERO)
                .activated(true)
                .build();
    }

    // (3) chamado pelo webhook quando o Mercado Pago confirma o pagamento
    @Transactional
    public void confirmPayment(String paymentId) throws Exception {
        MercadoPagoConfig.setAccessToken(accessToken);

        // busca o pagamento no Mercado Pago para confirmar
        PaymentClient client = new PaymentClient();
        Payment payment = client.get(Long.parseLong(paymentId));

        // só ativa se o status for "approved"
        if (!"approved".equals(payment.getStatus())) {
            System.out.println("Pagamento não aprovado: " + payment.getStatus());
            return;
        }

        // busca a subscription pelo ID do pagamento
        Subscription subscription = subscriptionRepository
                .findByMercadoPagoPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Subscription não encontrada"));

        // ativa o plano por 30 dias
        subscription.setType(SubscriptionType.ACTIVE);
        subscription.setActivatedAt(LocalDateTime.now());
        subscription.setExpiresAt(LocalDateTime.now().plusDays(30));
        subscriptionRepository.save(subscription);

        System.out.println("✅ Plano ativado para: " + subscription.getUser().getUsername());
    }

    // (4) retorna o plano ativo do usuário
    public Subscription getActiveSubscription(User user) {
        return subscriptionRepository
                .findByUserAndStatus(user, SubscriptionType.ACTIVE)
                .orElse(null);
    }

    // (5) verifica se o usuário pode criar mais dívidas
    public boolean canCreateDebt(User user, long currentDebtCount) {
        Subscription subscription = getActiveSubscription(user);

        // sem assinatura ativa usa limite do FREE
        if (subscription == null) {
            return currentDebtCount < PlanType.FREE.debtLimit();
        }

        int limit = subscription.getPlan().getType().debtLimit();
        return currentDebtCount < limit;
    }
}
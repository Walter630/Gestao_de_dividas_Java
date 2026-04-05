package com.Gestao_de_Contas.modules.debt.useCase;

import com.Gestao_de_Contas.modules.client.entity.Client;
import com.Gestao_de_Contas.modules.client.repository.ClientRepository;
import com.Gestao_de_Contas.modules.debt.dto.CreateDebtDTO;
import com.Gestao_de_Contas.modules.debt.dto.DebtBreakDown;
import com.Gestao_de_Contas.modules.debt.dto.DebtResponseDTO;
import com.Gestao_de_Contas.modules.debt.entity.Debt;
import com.Gestao_de_Contas.modules.debt.entity.StatusDivida;
import com.Gestao_de_Contas.modules.debt.repository.DebtRepository;
import com.Gestao_de_Contas.modules.notification.entity.DebtNotificationEventEntity;
import com.Gestao_de_Contas.modules.payment.entity.Payment;
import com.Gestao_de_Contas.modules.payment.repository.PaymentRepository;
import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.security.PlanGuard;
import com.Gestao_de_Contas.security.RabbitMQConfig;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DebtUseCase {

    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ClientRepository clientRepository;
    private final PlanGuard planGuard;
    private DebtResponseDTO toDTO(Debt debt) {
        return new DebtResponseDTO(
                debt.getId(),
                debt.getDescricao(),
                debt.getDevedorName(),
                debt.getValorOriginal(),
                debt.getStatus(),
                debt.getDataVencimento(),
                debt.getTaxType(),
                debt.getTaxJuros(),
                debt.getNumeroParcelas(),
                debt.getClient().getId(),
                debt.getClient().getName(),
                debt.getCreateAt(),
                debt.getUpdateAt()
        );
    }
    @Transactional
    // converte DTO para entidade
    public DebtResponseDTO createDebt(@Valid CreateDebtDTO dto, User userLogado) {
        //faz a validacao do plano

        planGuard.checkDebtLimit(userLogado);

        Client client = clientRepository.findById(dto.getClientId()).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        Debt debt = Debt.builder()
                .client(client)
                .user(userLogado)
                .valorOriginal(dto.getValorOriginal())
                .descricao(dto.getDescricao())
                .dataVencimento(dto.getDataVencimento())
                .taxType(dto.getTaxType())
                .taxJuros(dto.getTaxJuros())
                .numeroParcelas(dto.getNumeroParcelas())
                .status(StatusDivida.PENDENTE)
                .build();

        Debt savedDebt = debtRepository.save(debt);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING,
                new DebtNotificationEventEntity(
                        userLogado.getEmail(),
                        client.getName(),
                        savedDebt.getValorOriginal(),
                        savedDebt.getDataVencimento(),
                        savedDebt.getStatus().toString()
                )
        );
        return toDTO(savedDebt);
    }

    public DebtBreakDown calcJurosMensal(Debt debt) {
        BigDecimal valueOriginal = debt.getValorOriginal();

        BigDecimal totalJurosPay = debt.getPayments().stream()
                .map(Payment::getTaxValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPrincipalPago = debt.getPayments().stream()
                .map(Payment::getValuePrincipal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal jurosAcumulado = calcularJuros(valueOriginal, debt); // ← agora usa dias fracionados

        BigDecimal jurosPendentes = jurosAcumulado
                .subtract(totalJurosPay)
                .max(BigDecimal.ZERO);

        BigDecimal saldoPrincipal = valueOriginal
                .subtract(totalPrincipalPago)
                .max(BigDecimal.ZERO);

        return new DebtBreakDown(
                valueOriginal, jurosAcumulado, totalJurosPay,
                totalPrincipalPago, saldoPrincipal, jurosPendentes
        );
    }

    //juros simples
    public BigDecimal calcularJuros(BigDecimal valueOriginal, Debt debt) {
        long dias = ChronoUnit.DAYS.between(debt.getDataVencimento(), LocalDateTime.now());

        BigDecimal mesesFracionados = new BigDecimal(Math.max(0, dias))
                .divide(new BigDecimal(30), 4, RoundingMode.HALF_UP);
        return valueOriginal
                .multiply(debt.getTaxJuros())
                .multiply(mesesFracionados)
                .setScale(2, RoundingMode.HALF_UP);
    }
    public Debt getDebtId(UUID uuid) {
        return debtRepository.findById(uuid).orElseThrow(() -> new RuntimeException(("divida not validate")));
    }
    // DebtUseCase.java
    public Debt getDebtIdByUser(UUID debtId, UUID userId) {
        Debt debt = getDebtId(debtId);
        if (!debt.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado"); // vira 403
        }
        return debt;
    }

    public DebtResponseDTO getDebtByUser(UUID debtId, UUID userId) {
        return toDTO(getDebtIdByUser(debtId, userId)); // reutiliza o de cima
    }
    @Transactional //tudo ou nada
    public Payment addPayment(UUID debtId, Payment paymentDTO) {
        Debt debt = getDebtId(debtId);
        paymentDTO.setDebt(debt);
        paymentDTO.setPaymentDate(LocalDateTime.now());
        Payment saved = paymentRepository.save(paymentDTO);

        updateStatusDTO(debt); //atualiza os status automaticamente
        return saved;
    }

    public Boolean isDebtQuick(Debt debt) {
        DebtBreakDown breakDown = calcJurosMensal(debt);
        return breakDown.getSaldoPrincipal().compareTo(BigDecimal.ZERO) == 0
                && breakDown.getJurosPendentes().compareTo(BigDecimal.ZERO) == 0;
    }

    public BigDecimal calcularProximaParcela(Debt debt) {
        DebtBreakDown breakdown = calcJurosMensal(debt);
        // parcela = principal dividido pelo número de meses + juros do mês
        BigDecimal parcelaPrincipal = breakdown.getSaldoPrincipal()
                .divide(new BigDecimal(debt.getNumeroParcelas()), 2, RoundingMode.HALF_UP);
        BigDecimal jurosMes = breakdown.getSaldoPrincipal()
                .multiply(debt.getTaxJuros())
                .setScale(2, RoundingMode.HALF_UP);
        return parcelaPrincipal.add(jurosMes);
    }

    public Debt updateStatus(Debt debt) {
        //se tiver quitada a divida entao os status ta ok
        if (isDebtQuick(debt)) {
            debt.setStatus(StatusDivida.PAGO);
            //se a data de agora tiver passado da data de vencimento entt ta atrasado
        } else if (LocalDateTime.now().isAfter(debt.getDataVencimento())) {
            debt.setStatus(StatusDivida.ATRASADO);
            // se nao for vazia mais tiver pagamento entt ta parcial
        } else if (!debt.getPayments().isEmpty()) {
            debt.setStatus(StatusDivida.PARCIAL);
        } else { //se nao tiver nenhum caso desses ela ta pendente para ser paga
            debt.setStatus(StatusDivida.PENDENTE);
        }
        Debt saved = debtRepository.save(debt);

        // publicar na fila do rabbit
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE, //troca de mensagem, troca de informaçao
                RabbitMQConfig.ROUTING, //
                new DebtNotificationEventEntity(
                        debt.getUser().getUsername(),
                        debt.getClient().getName(),
                        calcJurosMensal(debt).getSaldoPrincipal(),
                        debt.getDataVencimento(),
                        debt.getStatus().toString()
                )
        );
        return debt;
    }

    // updateStatusDTO também precisa existir
    public DebtResponseDTO updateStatusDTO(Debt debt) {
        return toDTO(updateStatus(debt));
    }

    public List<DebtResponseDTO> listarTodas(User userId) {
        return debtRepository.findByUser(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

}

package com.Gestao_de_Contas.modules.notification;

import com.Gestao_de_Contas.modules.notification.entity.DebtNotificationEventEntity;
import com.Gestao_de_Contas.modules.notification.useCase.EmailService;
import com.Gestao_de_Contas.security.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onNotification(DebtNotificationEventEntity event) {
        emailService.enviarEmail(
                event.getEmailId(),
                buildAssunto(event),
                buildMensagem(event)
        );
    }

    private String buildAssunto(DebtNotificationEventEntity event) {
        return switch (event.getTipo()) {
            case "ATRASADO" -> "Divida em atraso!";
            case "PENDENTE" -> "Divida esta Pendente";
            case "PAGO" -> "Divida Quitada!!";
            case "PARCIAL" -> "Divida em parcial";
            default -> "Atualização da divida";
        };
    }

    private String buildMensagem(DebtNotificationEventEntity event) {
        return switch (event.getTipo()) {
            case "ATRASADO" -> String.format("""
                    Olá!
                    
                                    A dívida do cliente %s está em atraso.
                                    Valor pendente: R$ %s
                                    Vencimento: %s
                    
                                    Acesse o sistema para mais detalhes.
                    """,
                    event.getClientName(),
                    event.getValorPendente(),
                    event.getDataVencimento()
            );
            case "PENDENTE" -> String.format("""
                    Olá!
                    
                                    A dívida do cliente %s vence em breve.
                                    Valor: R$ %s
                                    Vencimento: %s
                    
                                    Acesse o sistema para mais detalhes.
                    """,
                    event.getClientName(),
                    event.getValorPendente(),
                    event.getDataVencimento()
            );
            case "PARCIAL" -> String.format("""
                        Ola!
                            A divida do cliente %s vence em breve.
                            Valor: R$ %s
                            Vencimento: %s
                            
                            Voce ja fez o pagamento de alguma parcela agora falta
                            finalizar o resto.
                    """,
                    event.getClientName(),
                    event.getValorPendente(),
                    event.getDataVencimento()
            );

            case "QUITADA" -> String.format("""
                    Olá!
                    
                    A dívida do cliente %s foi totalmente quitada!
                    
                    Acesse o sistema para mais detalhes.
                    """,
                    event.getClientName()
            );
            default -> "Você tem uma atualização na sua dívida.";
        };
    }
}

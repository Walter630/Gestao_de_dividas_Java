package com.Gestao_de_Contas.modules.notification.useCase;

import com.Gestao_de_Contas.modules.client.entity.ClientNotificationEventEntity;
import com.Gestao_de_Contas.security.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
@RequiredArgsConstructor
public class ClientConsumer {
        private final EmailService emailService;

        @RabbitListener(queues = RabbitMQConfig.CLIENTE_QUEUE)
        public void onClientCreated(ClientNotificationEventEntity event) {
            try {
                emailService.enviarEmail(
                        event.getNameClient(),  // email do cliente
                        "Novo cliente cadastrado no Gestão de Contas!",
                        buildMensagemCliente(event)
                );
            }catch (Exception e) {
                System.err.println("Erro ao enviar e-mail para " + event.getNameClient() + ": " + e.getMessage());
            }
        }

    private String buildMensagemCliente(ClientNotificationEventEntity event) {
        return String.format("""
              Olá %s!
              
              Tudo certo! O cliente %s foi cadastrado com sucesso no seu sistema.
              📱 Telefone registrado: %s
              
              A partir de agora, você já pode acessar o sistema para iniciar a gestão de dívidas deste cliente. Você pode registrar novos valores pendentes, acompanhar datas de vencimento e ter o controle total dos pagamentos e parcelas.
              
              Bons negócios e ótima gestão!
              
              Equipe Gestão de Contas
              """,
                event.getEmailUser(),
                event.getNameClient(), // Nome do cliente cadastrado
                event.getTelefoneClient() // Telefone do cliente
        );
    }
    }


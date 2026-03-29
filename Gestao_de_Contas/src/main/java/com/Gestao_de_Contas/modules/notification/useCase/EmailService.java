package com.Gestao_de_Contas.modules.notification.useCase;

import com.Gestao_de_Contas.modules.notification.entity.DebtNotificationEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.notification.destinatario}")
    private String username;

    public void enviarEmail(String emailDoCliente, String assunto, String mensagem) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(username);        // ✅ sempre pro seu email
        message.setSubject(assunto);    // ✅ vem do buildAssunto()
        message.setText(mensagem);      // ✅ vem do buildMensagem()
        mailSender.send(message);
        System.out.println("✅ Email enviado para: " + username + " | Assunto: " + assunto);
    }

}

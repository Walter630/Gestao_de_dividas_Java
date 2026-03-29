package com.Gestao_de_Contas.modules.notification.useCase;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviarEmail(String email, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(assunto);
        message.setText(corpo);
        mailSender.send(message);
    }
}

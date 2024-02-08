package com.microservice.fakeapi.business.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${send.mail.from}")
    private String mailFrom;

    @Value("${send.mail.to}")
    private String mailTo;

    public void enviaEmailExcecao(Exception e){ // Método para enviar e-mail em caso de exceção
        try {
            final MimeMessage message = javaMailSender.createMimeMessage();  // Cria uma nova mensagem de e-mail MIME
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());  // Inicializa um MimeMessageHelper para facilitar a construção da mensagem

            mimeMessageHelper.setFrom(new InternetAddress(mailFrom));  // Define o remetente do e-mail
            mimeMessageHelper.setTo(InternetAddress.parse(mailTo));  // Define o destinatário do e-mail
            mimeMessageHelper.setSubject("Notificação de erro no sistema");  // Define o assunto do e-mail
            mimeMessageHelper.setText("Ocorreu um erro no sistema" + "\n" + e.getMessage() + "\n" + LocalDateTime.now());  // Define o corpo do e-mail, incluindo a mensagem de exceção e a data e hora atual

            javaMailSender.send(message);  // Envia a mensagem de e-mail usando o JavaMailSender

        }catch (MessagingException ex){  // Captura exceções relacionadas ao envio de e-mail
            ex.printStackTrace();  // Imprime o rastreamento da pilha da exceção
        }
    }
}

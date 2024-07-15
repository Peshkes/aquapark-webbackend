package ru.kikopark.backend.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {

    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String toEmail, byte[] attachmentData, String attachmentName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("website@kikopark.ru");
        helper.setTo(toEmail);
        helper.setSubject("Ваш билет в аквапарк КИКО");
        helper.setText("Билет во вложениях)");

        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentData));

        mailSender.send(message);
    }
}

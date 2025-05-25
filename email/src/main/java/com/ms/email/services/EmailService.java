package com.ms.email.services;

import com.ms.email.model.EmailModel;
import com.ms.email.model.enums.StatusEmail;
import com.ms.email.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public EmailService(EmailRepository emailRepository, JavaMailSender javaMailSender) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public EmailModel sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSendDateTime(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailModel.getEmailTo());
            mailMessage.setSubject(emailModel.getSubject());
            mailMessage.setText(emailModel.getText());
            javaMailSender.send(mailMessage);

            emailModel.setStatusEmail(StatusEmail.SENT);
        }
        catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
        }
        finally {
            return emailRepository.save(emailModel);
        }
    }


}

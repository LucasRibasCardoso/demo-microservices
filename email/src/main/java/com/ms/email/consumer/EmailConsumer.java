package com.ms.email.consumer;

import com.ms.email.dto.EmailDTO;
import com.ms.email.model.EmailModel;
import com.ms.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDTO emailDTO) {
        // Converte dto para model
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailDTO, emailModel);

        // Envia o email
        emailService.sendEmail(emailModel);
    }
}

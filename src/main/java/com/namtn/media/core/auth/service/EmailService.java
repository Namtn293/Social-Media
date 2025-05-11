package com.namtn.media.core.auth.service;

import com.namtn.media.core.model.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = BusinessException.class)
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    public void sendEmail(String to,String subject,String content) throws MessagingException {
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(to);
        helper.setFrom("tnnam2903@gmail.com");
        helper.setSubject(subject);
        helper.setText(content,true);
        javaMailSender.send(message);
    }
}

package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.Mail;
import com.quocnguyen.koko.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${koko.email.from}")
    private String DEFAULT_MAIL_FROM;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String mailFrom = mail.getMailFrom() != null ? mail.getMailFrom() : DEFAULT_MAIL_FROM;
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            messageHelper.setSubject(mail.getMainSubject());
            messageHelper.setFrom(new InternetAddress(mailFrom));
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setText(mail.getMainContent(), mail.isHtml());

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

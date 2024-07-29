package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.Mail;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */
public interface MailService {

    void sendEmail(Mail mail);
}

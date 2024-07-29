package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.Mail;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.model.VerificationCode;
import com.quocnguyen.koko.repository.VerificationCodeRepository;
import com.quocnguyen.koko.service.MailService;
import com.quocnguyen.koko.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Random;


/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@RequiredArgsConstructor
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final static String TOKEN_CHARACTER_DOMAIN = "0123456789";
    private final static int TOKEN_LENGTH = 4;
    private final static int NUM_TRIAL = 3;
    private final static long EXPIRE_TIME = 36000000;
    private final VerificationCodeRepository codeRepo;
    private final MailService mailService;

    @Override
    public void saveAndSendToken() {
        // DO some stuff
    }

    /**
     * This method helps to create a random token string with configured length
     *
     * @return a token with given length
     */
    protected String createRandomToken() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < TOKEN_LENGTH; i++) {
            sb.append(TOKEN_CHARACTER_DOMAIN.charAt(random.nextInt(TOKEN_LENGTH)));
        }

        return sb.toString();
    }

    /**
     * create a verify code
     *
     * @param user
     * @param codeType
     * @return
     */
    protected VerificationCode createCode(UserDTO user, VerificationCode.CodeType codeType) {

        User savedUser = new User();
        BeanUtils.copyProperties(user, savedUser);

        VerificationCode code = VerificationCode
                .builder()
                .token(createRandomToken())
                .user(savedUser)
                .codeType(codeType)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(EXPIRE_TIME, ChronoUnit.MILLIS))
                .numTrial(NUM_TRIAL)
                .build();

        return code;
    }

    @Override
    public VerificationCode createVerifyEmailCodeAndSendEmail(UserDTO user) {
        VerificationCode code = createCode(user, VerificationCode.CodeType.VERIFICATION_EMAIL);

        codeRepo.save(code);

        Mail mail = Mail
                .builder()
                .mainSubject("Confirm your account email")
                .mailTo(user.getEmail())
                .isHtml(true)
                .mainContent(buildVerificationAccountEmail(code))
                .build();

        mailService.sendEmail(mail);

        return code;
    }

    private String buildVerificationAccountEmail(VerificationCode code) {
        return "<div>\n" +
                "   <span> Hello, " + code.getUser().getName() + "</span>\n" +
                "   <p>You have created an account on our system. Here is your confirmation code. Please don't share to anyone</p>" +
                "   <p>" + code.getToken() +  "</p";
    }
}

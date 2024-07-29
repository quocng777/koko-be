package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.model.VerificationCode;
import org.yaml.snakeyaml.tokens.Token;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */
public interface VerificationCodeService {
    void saveAndSendToken();
    VerificationCode createVerifyEmailCodeAndSendEmail(UserDTO user);
}

package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.LoginParams;
import com.quocnguyen.koko.dto.RefreshTokenParams;
import com.quocnguyen.koko.dto.SignupPrams;

import java.util.Map;

public interface AuthService {
    Map<String, String> signup(SignupPrams request);

    Map<String, String> login(LoginParams request);

    Map<String, String> refreshToken(RefreshTokenParams refreshParams);
}

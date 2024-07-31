package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.LoginParams;
import com.quocnguyen.koko.dto.SignupPrams;

import java.util.Map;

public interface AuthService {
    Map<String, String> signup(SignupPrams request);

    Map<String, String> login(LoginParams request);
}

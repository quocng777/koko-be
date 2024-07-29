package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.SignUpRequest;
import com.quocnguyen.koko.dto.UserDTO;

import java.util.Map;

public interface AuthService {
    Map<String, String> signup(SignUpRequest request);
}

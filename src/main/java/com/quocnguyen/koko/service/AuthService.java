package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.SignUpRequest;
import com.quocnguyen.koko.dto.UserDTO;

public interface AuthService {
    UserDTO signup(SignUpRequest request);
}

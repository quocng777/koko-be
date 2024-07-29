package com.quocnguyen.koko.controller;


import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.SignUpRequest;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.service.AuthService;
import com.quocnguyen.koko.service.VerificationCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<AppResponse<Map<String, String>>> signup(@Valid @RequestBody SignUpRequest request) {

        Map<String, String> tokens = authService.signup(request);

        return new ResponseEntity<>(AppResponse.created(tokens, "User created successfully"), HttpStatus.CREATED);
    }
}

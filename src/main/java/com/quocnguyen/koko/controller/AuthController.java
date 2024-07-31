package com.quocnguyen.koko.controller;


import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.ErrorResponse;
import com.quocnguyen.koko.dto.LoginParams;
import com.quocnguyen.koko.dto.SignupPrams;
import com.quocnguyen.koko.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody SignupPrams request) {

        Map<String, String> tokens = authService.signup(request);

        return new ResponseEntity<>(AppResponse.created(tokens, "User created successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginParams loginParams) {
        try {
            var tokens = authService.login(loginParams);
            return ResponseEntity.ok(AppResponse.success(tokens));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN, "Access denied"), HttpStatus.FORBIDDEN);
        }
    }
}

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final VerificationCodeService vcService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<AppResponse<UserDTO>> signup(@Valid @RequestBody SignUpRequest request) {

        UserDTO savedUser = authService.signup(request);
        vcService.createVerifyEmailCodeAndSendEmail(savedUser);

        return new ResponseEntity<>(AppResponse.created(savedUser, "User created successfully"), HttpStatus.CREATED);
    }
}

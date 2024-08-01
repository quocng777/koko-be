package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.LoginParams;
import com.quocnguyen.koko.dto.RefreshTokenParams;
import com.quocnguyen.koko.dto.SignupPrams;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.exception.ApiException;
import com.quocnguyen.koko.exception.ErrorCode;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.RefreshToken;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.RefreshTokenRepository;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.service.AuthService;
import com.quocnguyen.koko.service.VerificationCodeService;
import com.quocnguyen.koko.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${koko.jwt.refresh-expiration}")
    private long REFRESH_EXPIRATION_TIME;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepo;
    private final VerificationCodeService vcService;
    private final AuthenticationManager authManager;

    /**
     * This method helps to create new user in the system
     *
     * @param request: information of new user
     * @return a user dto object if the process is success
     */
    @Override
    public Map<String, String> signup(SignupPrams request) {

        if(!checkValidEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.EXISTED_USERNAME, String.format("Email %s is used by another user", request.getEmail()));
        }

        if(!checkValidUsername(request.getUsername())) {
            throw new ApiException(ErrorCode.EXISTED_USERNAME, String.format("Username %s is used by another user", request.getUsername()));
        }
        User user = new User();

        // copy property of the request to the user entity
        BeanUtils.copyProperties(request, user);

        // set new hashed password to the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setEnabled(true);

        userRepo.save(user);

        // generate tokens (access token and refresh token)
        Map<String, String> tokens = generateTokens(user);

        // handle sending email to verify
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        vcService.createVerifyEmailCodeAndSendEmail(userDTO);


        return tokens;
    }

    public boolean checkValidEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        return user == null;
    }

    public boolean checkValidUsername(String username) {
        User user = userRepo.findByUsername(username).orElse(null);

        return user == null;
    }

    private Map<String, String> generateTokens(User user) {
        String accessToken = jwtUtils.generateTokeFromUsername(user.getUsername());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(new Date())
                .expireAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .build();

        // delete previous token if it exists
        refreshTokenRepo.deleteByUserId(user.getId());

        refreshTokenRepo.save(refreshToken);

        Map<String, String> tokens = new HashMap<>();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken.getToken());


        return tokens;
    }

    /**
     * login user by using name and password
     *
     * @param request
     * @return
     */

    @Override
    public Map<String, String> login(LoginParams request) {

        var authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication auth =  authManager.authenticate(authToken);
        var cxt = SecurityContextHolder.createEmptyContext();
        cxt.setAuthentication(auth);

        User user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("user name not found"));

        var tokenMap = generateTokens(user);

        return tokenMap;
    }

    @Override
    public Map<String, String> refreshToken(RefreshTokenParams refreshParams) {
        RefreshToken token = refreshTokenRepo
                .findByUsername(refreshParams.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if(!token.getToken().equals(refreshParams.getToken())) {
            return null;
        }else if(token.isExpired()) {
            refreshTokenRepo.delete(token);
            return null;
        }

        return generateTokens(token.getUser());
    }
}

package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.SignUpRequest;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.exception.ApiException;
import com.quocnguyen.koko.exception.ErrorCode;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method helps to create new user in the system
     *
     * @param request: information of new user
     * @return a user dto object if the process is success
     */
    @Override
    public UserDTO signup(SignUpRequest request) {

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

        userRepo.save(user);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        return userDTO;
    }

    public boolean checkValidEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        return user == null;
    }

    public boolean checkValidUsername(String username) {
        User user = userRepo.findByUsername(username).orElse(null);

        return user == null;
    }
}

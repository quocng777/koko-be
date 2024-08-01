package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */
public interface UserService {

    UserDetails loadByUsername(String username);

    UserDTO verifyAccount(String token);

    UserDTO getAuthenticatedUser();
}

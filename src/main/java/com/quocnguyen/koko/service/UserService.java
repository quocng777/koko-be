package com.quocnguyen.koko.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */
public interface UserService {

    UserDetails loadByUserName(String username);
}

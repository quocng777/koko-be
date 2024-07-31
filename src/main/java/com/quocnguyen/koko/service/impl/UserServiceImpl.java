package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.model.AppUserDetails;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    @Override
    public UserDetails loadByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(AppUserDetails::new)
                .orElse(null);

    }
}

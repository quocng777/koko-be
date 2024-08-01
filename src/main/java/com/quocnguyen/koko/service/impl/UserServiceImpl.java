package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.model.AppUserDetails;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.model.VerificationCode;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.repository.VerificationCodeRepository;
import com.quocnguyen.koko.service.UserService;
import com.quocnguyen.koko.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final VerificationCodeRepository vcRepo;

    @Override
    public UserDetails loadByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(AppUserDetails::new)
                .orElse(null);

    }

    @Override
    public UserDTO verifyAccount(String token) {
        UserDetails userDetails = getUserFromCxt();

        User user = userRepo
                .findByUsername(userDetails.getUsername())
                .orElse(null);
        Assert.notNull(user, "The user must not null");

        if(!user.isVerified()) {
            VerificationCode verificationCode = vcRepo
                    .findByUserIdAAndCodeType(user.getId(), VerificationCode.CodeType.VERIFICATION_EMAIL)
                    .orElse(null);

            if(verificationCode == null)
                return null;

            if(verificationCode.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
                vcRepo.delete(verificationCode);
                return null;
            }

            if(!verificationCode.getToken().equals(token)) {
                if(verificationCode.getNumTrial() <= 1) {
                    vcRepo.delete(verificationCode);
                } else {
                    verificationCode.setNumTrial(verificationCode.getNumTrial() - 1);
                    vcRepo.save(verificationCode);
                }

                return null;
            }

            vcRepo.delete(verificationCode);
            user.setVerified(true);
            userRepo.save(user);
        }

        UserDTO returnObj = new UserDTO();
        BeanUtils.copyProperties(user, returnObj);

        return returnObj;
    }

    private UserDetails getUserFromCxt() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public UserDTO getAuthenticatedUser() {
        UserDetails userDetails = getUserFromCxt();

        User user = userRepo.findByUsername(userDetails.getUsername()).orElse(null);

        Assert.notNull(user, "User from context must not null in database");

        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);

        return dto;
    }
}

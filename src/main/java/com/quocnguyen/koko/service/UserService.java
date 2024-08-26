package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.UserContactDTO;
import com.quocnguyen.koko.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */
public interface UserService {

    UserDetails loadByUsername(String username);

    UserDTO verifyAccount(String token);

    UserDTO getAuthenticatedUser();

    AppPaging<UserContactDTO> getFriends(String keyword,
                                         int pageNum,
                                         int pageSize);
}

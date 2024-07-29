package com.quocnguyen.koko.auth;

import com.quocnguyen.koko.dto.SignUpRequest;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.UserRepository;
import com.quocnguyen.koko.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Quoc Nguyen on {7/27/2024}
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthServiceTest {
    @Autowired
    private AuthService service;
    @Autowired
    private UserRepository repo;

//    @Test
//    public void signupNewUser() {
//        SignUpRequest request = new SignUpRequest(
//                "Quoc Nguyen",
//                "quocng777",
//                "quocng777@gmail.com",
//                "quoc8338204",
//                0,
//                new Date());
//
//        UserDTO user = service.signup(request);
//        System.out.println(user);
//        assertThat(user).isNotNull();
//    }
}

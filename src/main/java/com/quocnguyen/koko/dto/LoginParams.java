package com.quocnguyen.koko.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author Quoc Nguyen on {7/31/2024}
 */

@Getter
@Setter
public class LoginParams {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

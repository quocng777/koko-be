package com.quocnguyen.koko.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Quoc Nguyen on {8/1/2024}
 */

@Getter
@Setter
public class RefreshTokenParams {
    private String username;
    private String token;
}

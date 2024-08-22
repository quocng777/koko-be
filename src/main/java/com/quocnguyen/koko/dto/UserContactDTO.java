package com.quocnguyen.koko.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Builder
@Data
public class UserContactDTO {
    private Long id;
    private String username;
    private String name;
    private String avatar;
}

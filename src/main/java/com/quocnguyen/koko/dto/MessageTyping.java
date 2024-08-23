package com.quocnguyen.koko.dto;

import lombok.Data;

/**
 * @author Quoc Nguyen on {2024-08-23}
 */

@Data
public class MessageTyping {
    private Long conservation;
    private Long user;
    private boolean status;
}

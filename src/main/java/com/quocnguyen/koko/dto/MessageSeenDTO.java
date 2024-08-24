package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-24}
 */

@Data
@Builder
public class MessageSeenDTO {
    private Long user;
    private Long message;
    private Long conservation;
    private Date seenAt;
}

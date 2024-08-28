package com.quocnguyen.koko.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@Data
@Builder
public class UserFriendDTO {
    private UserContactDTO relatedUser;
    private UserContactDTO.FriendStatus friendStatus;
    private Date createdAt;
}

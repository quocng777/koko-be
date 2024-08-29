package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.User;
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
    private FriendStatus friendStatus;

    public enum FriendStatus {
        FRIEND,
        SENT_REQUEST,
        RECEIVED_REQUEST,
        STRANGER,
    }

    public static UserContactDTO convert(User user) {
        return UserContactDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }

}

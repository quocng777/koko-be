package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.Notification;
import com.quocnguyen.koko.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private UserContactDTO publishedUser;
    private Long recipient;
    private String objectUrl;
    private Long objectId;
    private Notification.NotificationType type;
    private Date seenAt;
    private Date dismissedAt;
    private Date createdAt;

    public enum NotificationType {
        FRIEND_REQUEST,
        FRIEND_ACCEPT,
    }
}

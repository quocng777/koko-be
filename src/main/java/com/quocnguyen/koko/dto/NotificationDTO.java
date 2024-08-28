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

    public static NotificationDTO convert(Notification notification) {
        return NotificationDTO
                .builder()
                .id(notification.getId())
                .publishedUser(UserContactDTO
                        .builder()
                        .id(notification.getPublishedUser().getId())
                        .name(notification.getPublishedUser().getUsername())
                        .name(notification.getPublishedUser().getName())
                        .avatar(notification.getPublishedUser().getAvatar())
                        .build())
                .recipient(notification.getRecipient().getId())
                .objectUrl(notification.getObjectUrl())
                .objectId(notification.getObjectId())
                .type(notification.getType())
                .seenAt(notification.getSeenAt())
                .dismissedAt(notification.getDismissedAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

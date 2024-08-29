package com.quocnguyen.koko.event.listener;

import com.quocnguyen.koko.dto.NotificationDTO;
import com.quocnguyen.koko.dto.UserContactDTO;
import com.quocnguyen.koko.event.FriendAcceptEvent;
import com.quocnguyen.koko.event.FriendRequestEvent;
import com.quocnguyen.koko.model.Notification;
import com.quocnguyen.koko.model.Relationship;
import com.quocnguyen.koko.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@RequiredArgsConstructor
@Slf4j
@Component
public class FriendEventListener {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener(classes = FriendRequestEvent.class)
    public void handleFriendRequest(FriendRequestEvent event) {
        var publishedUser = event.getRelationship().getUser();
        NotificationDTO notificationDTO = NotificationDTO
                .builder()
                .publishedUser(
                        UserContactDTO.convert(publishedUser)
                )
                .recipient(event.getRelationship().getRelatedUser().getId())
                .createdAt(event.getRelationship().getCreatedAt())
                .objectId(publishedUser.getId())
                .objectUrl("/users/" + publishedUser.getUsername())
                .type(Notification.NotificationType.FRIEND_REQUEST)
                .build();
        Long notificationId = notificationService.save(notificationDTO);

        notificationDTO.setId(notificationId);

        messagingTemplate.convertAndSendToUser(notificationDTO.getRecipient().toString(), "/notification", notificationDTO);
    }

    @EventListener(classes = FriendAcceptEvent.class)
    public void handleFriendAcceptEvent(FriendAcceptEvent event) {
        Relationship rel = event.getRelationship();

        var publishedUser = event.getRelationship().getUser();
        NotificationDTO notificationDTO = NotificationDTO
                .builder()
                .publishedUser(
                        UserContactDTO.convert(publishedUser)
                )
                .recipient(event.getRelationship().getRelatedUser().getId())
                .createdAt(event.getRelationship().getCreatedAt())
                .objectId(publishedUser.getId())
                .objectUrl("/users/" + publishedUser.getUsername())
                .type(Notification.NotificationType.FRIEND_ACCEPT)
                .build();

        Long notificationId = notificationService.save(notificationDTO);

        notificationDTO.setId(notificationId);

        messagingTemplate.convertAndSendToUser(notificationDTO.getRecipient().toString(), "/notification", notificationDTO);
    }
}

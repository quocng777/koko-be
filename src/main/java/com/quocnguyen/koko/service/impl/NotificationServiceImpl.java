package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.NotificationDTO;
import com.quocnguyen.koko.model.Notification;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.NotificationRepository;
import com.quocnguyen.koko.service.NotificationService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepo;
    private final UserService userService;

    @Override
    public Long save(NotificationDTO notification) {
        Notification notificationEntity = Notification
                .builder()
                .publishedUser(new User(notification.getPublishedUser().getId()))
                .recipient(new User(notification.getRecipient()))
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .seenAt(notification.getSeenAt())
                .dismissedAt(notification.getDismissedAt())
                .objectId(notification.getObjectId())
                .objectUrl(notification.getObjectUrl())
                .build();

        notificationEntity = notificationRepo.save(notificationEntity);

        return notificationEntity.getId();
    }

    @Override
    public int numberUndismissedNotification() {
        var user = userService.getAuthenticatedUser();
        return notificationRepo.countNumberUnDismissed(user.getId());
    }
}

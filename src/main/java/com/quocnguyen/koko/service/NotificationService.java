package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.NotificationDTO;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

public interface NotificationService {
    Long save(NotificationDTO notification);
    int numberUndismissedNotification();

    AppPaging<NotificationDTO> getNotifications(int pageNum, int pageSize, Long beforeId);
}

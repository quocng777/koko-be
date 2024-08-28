package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/undismissed")
    public ResponseEntity<?> getUnDismissedNotification() {
        return ResponseEntity.ok(
                AppResponse.success(notificationService.numberUndismissedNotification())
        );
    }

    @GetMapping("")
    public ResponseEntity<?> getNotifications(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                              @RequestParam(name = "beforeId", required = false) Long beforeId) {

        return ResponseEntity.ok(
                AppResponse.success(notificationService.getNotifications(pageNum, pageSize, beforeId)
        ));
    }


}

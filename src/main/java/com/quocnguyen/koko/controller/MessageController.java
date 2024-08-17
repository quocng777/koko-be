package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO message) {
        var savedMessage = messageService.create(message);

        return ResponseEntity.ok(AppResponse.success(savedMessage));
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestMessage(@RequestParam Long conservation) {
        return ResponseEntity.ok(
                AppResponse.success(messageService.getLatestMessage(conservation))
        );
    }
}

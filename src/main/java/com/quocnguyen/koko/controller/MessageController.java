package com.quocnguyen.koko.controller;

import com.quocnguyen.koko.dto.AppResponse;
import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.dto.MessageQueryParams;
import com.quocnguyen.koko.dto.MessageTyping;
import com.quocnguyen.koko.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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

    @GetMapping("")
    public ResponseEntity<?> getMessages(@ModelAttribute MessageQueryParams messageQueryParams,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum) {

        return ResponseEntity
                .ok(AppResponse
                        .success(messageService
                                .getMessages(messageQueryParams, pageSize, pageNum)));
    }

    @GetMapping("/seen-status")
    public ResponseEntity<?> updateSeenStatus(@RequestParam("conservation") Long conservationId) {
        return ResponseEntity.ok(
                AppResponse.success(messageService.updateSeenStatus(conservationId)));
    }

    @GetMapping("/unseen")
    public ResponseEntity<?> getNumberUnseenMsg(@RequestParam("conservation") Long conservationId) {
        return ResponseEntity.ok(
                AppResponse.success(messageService.getNumUnreadMessage(conservationId))
        );
    }

    @MessageMapping("/typing")
    public void sendTyping(@RequestBody MessageTyping messageTyping, Principal principal) {
        messageService.sendIsTyping(principal, messageTyping);
    }
}

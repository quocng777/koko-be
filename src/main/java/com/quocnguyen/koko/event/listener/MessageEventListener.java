package com.quocnguyen.koko.event.listener;

import com.quocnguyen.koko.event.MessageSeenEvent;
import com.quocnguyen.koko.event.MessageSendEvent;
import com.quocnguyen.koko.model.Conservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageEventListener {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @TransactionalEventListener(classes = MessageSendEvent.class)
    public void sendMessage(MessageSendEvent event) {
        var msg = event.getMessage();
        var conservation = event.getConservation();
        log.info("SEND MESSAGE EVENT: " + msg);

        conservation
                .getParticipants()
                .forEach((mem) -> {
                    Long memId = mem.getUser().getId();
                    simpMessagingTemplate.convertAndSendToUser(memId.toString(), "/message", msg);
                });
    }

    @EventListener(classes = MessageSeenEvent.class)
    public void handleMessageSeenEvent(MessageSeenEvent event) {
        var seenMsg = event.getMessageSeen();
        Conservation conservation = event.getConservation();

        conservation
                .getParticipants()
                .forEach((mem) -> {
                    Long memId = mem.getUser().getId();
                    simpMessagingTemplate.convertAndSendToUser(memId.toString(), "/seen", seenMsg);
                });
    }

}

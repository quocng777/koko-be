package com.quocnguyen.koko.event.listener;

import com.quocnguyen.koko.event.MessageSendEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSendListener {
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
                    if(!memId.equals(msg.getSender())) {
                        simpMessagingTemplate.convertAndSendToUser(memId.toString(), "/message", msg);
                    }
                });
    }

}

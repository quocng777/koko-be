package com.quocnguyen.koko.event.listener;

import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.MessageBroadcastDTO;
import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.event.GroupCreateEvent;
import com.quocnguyen.koko.model.BroadCastInfo;
import com.quocnguyen.koko.model.Message;
import com.quocnguyen.koko.service.MessageService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Quoc Nguyen on {2024-08-30}
 */


@RequiredArgsConstructor
@Slf4j
@Component
public class GroupEventListener {
    private final MessageService messageService;
    private final UserService userService;

    @EventListener(classes = GroupCreateEvent.class)
    public void handleGroupCreateEvent(GroupCreateEvent event) {
        ConservationDTO conservation = event.getConservation();
        UserDTO user = userService.getAuthenticatedUser();

        MessageBroadcastDTO messageBroadcast = MessageBroadcastDTO
                .builder()
                .broadcastType(BroadCastInfo.BroadCastType.CREATE)
                .build();

        MessageDTO messageDTO = MessageDTO
                .builder()
                .conservation(conservation.getId())
                .sender(user.getId())
                .broadcast(messageBroadcast)
                .type(Message.MessageType.BROADCAST)
                .build();

        messageService.create(messageDTO);
    }
}

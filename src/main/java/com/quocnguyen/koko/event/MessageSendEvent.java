package com.quocnguyen.koko.event;

import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.model.Conservation;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Getter
public class MessageSendEvent extends ApplicationEvent {
    private MessageDTO message;
    private Conservation conservation;
    public MessageSendEvent(Object source, MessageDTO message, Conservation conservation) {
        super(source);
        this.message = message;
        this.conservation = conservation;
    }
}

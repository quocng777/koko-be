package com.quocnguyen.koko.event;

import com.quocnguyen.koko.dto.MessageSeenDTO;
import com.quocnguyen.koko.model.Conservation;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Quoc Nguyen on {2024-08-24}
 */

@Getter
public class MessageSeenEvent extends ApplicationEvent {
    private MessageSeenDTO messageSeen;
    private Conservation conservation;

    public MessageSeenEvent(Object source, MessageSeenDTO messageSeen, Conservation conservation) {
        super(source);
        this.messageSeen = messageSeen;
        this.conservation = conservation;
    }
}

package com.quocnguyen.koko.event;

import com.quocnguyen.koko.dto.ConservationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Quoc Nguyen on {2024-08-30}
 */

@Getter
public class GroupCreateEvent extends ApplicationEvent {
    private ConservationDTO conservation;

    public GroupCreateEvent(Object source, ConservationDTO conservation) {
        super(source);
        this.conservation = conservation;
    }
}

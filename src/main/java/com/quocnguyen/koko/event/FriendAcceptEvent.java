package com.quocnguyen.koko.event;

import com.quocnguyen.koko.model.Relationship;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Quoc Nguyen on {2024-08-28}
 */

@Getter
public class FriendAcceptEvent extends ApplicationEvent {
    private Relationship relationship;

    public FriendAcceptEvent(Object source, Relationship relationship) {
        super(source);
        this.relationship = relationship;
    }
}

package com.quocnguyen.koko.event;

import com.quocnguyen.koko.model.Relationship;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@Getter
public class FriendRequestEvent extends ApplicationEvent {

    private Relationship relationship;

    public FriendRequestEvent(Object source, Relationship relationship) {
        super(source);
        this.relationship = relationship;
    }
}

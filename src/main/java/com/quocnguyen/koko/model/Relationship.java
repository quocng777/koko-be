package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-26}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Relationship {

    @EmbeddedId
    private RelationshipId id;

    @MapsId("user")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("relatedUser")
    @ManyToOne
    @JoinColumn(name = "related_user_id")
    private User relatedUser;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private RelationshipType type;

    private Date createdAt;

    public enum RelationshipType {
        FRIEND,
        MESSAGE_BLOCK,
    }

}

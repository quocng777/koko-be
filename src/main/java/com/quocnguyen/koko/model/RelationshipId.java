package com.quocnguyen.koko.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Quoc Nguyen on {2024-08-26}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RelationshipId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long user;

    @Column(name = "related_user_id", nullable = false)
    private Long relatedUser;
}

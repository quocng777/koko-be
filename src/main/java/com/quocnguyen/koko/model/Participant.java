package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conservation_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private Conservation conservation;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    public enum Role {
        ADMIN,
        MEMBER
    }

    private Date createAt;
}

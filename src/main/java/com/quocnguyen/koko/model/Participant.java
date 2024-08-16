package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conservation_id", referencedColumnName = "id")
    private Conservation conservation;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    public enum Role {
        ADMIN,
        MEMBER
    }

    private Date createAt;
}

package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author Quoc Nguyen on {2024-08-30}
 */

@Data
@Entity
public class BroadCastInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "broadcast_type")
    @Enumerated(value = EnumType.STRING)
    private BroadCastType broadCastType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "broadcast_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> targetUsers;

    public enum BroadCastType {
        CREATE,
        ADD_MEMBER,
        JOIN,
        LEAVE,
    }
}

package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-24}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seen_message")
public class SeenMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private Message message;

    private Date seenAt;
}

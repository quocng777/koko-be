package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conservation_id", referencedColumnName = "id")
    private Conservation conservation;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "sender_id")
    private User sender;

    private String message;


    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "message", fetch = FetchType.EAGER)
    private Set<Attachment> attachments;

    @OneToMany(mappedBy = "message", fetch = FetchType.EAGER)
    private Set<SeenMessage> seenUser;

    public enum MessageType {
        TEXT,
        IMAGE,
        FILE,
        VIDEO,
        VOICE
    }
}

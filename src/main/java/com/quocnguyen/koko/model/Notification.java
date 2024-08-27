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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "published_usser_id", referencedColumnName = "id")
    private User publishedUser;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;
    private String objectUrl;
    private Long objectId;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;
    private Date seenAt;
    private Date dismissedAt;
    private Date createdAt;

    public enum NotificationType {
        FRIEND_REQUEST,
        FRIEND_ACCEPT,
    }
}

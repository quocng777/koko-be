package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    private Message message;

    private String url;

    private Date createdAt;
}

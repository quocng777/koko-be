package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Quoc Nguyen on {7/28/2024}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "verification_code")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Date createdAt;
    private Date expiresAt;
    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    @Column(columnDefinition = "int default '3'")
    private int numTrial;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;

    public enum CodeType {
        VERIFICATION_EMAIL,
        CHANGE_PASSWORD
    }

}

package com.quocnguyen.koko.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @Enumerated(EnumType.STRING)
    private ConservationType type;

    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conservation")
    @EqualsAndHashCode.Exclude
    private Set<Participant> participants;

    public enum ConservationType {
        SINGLE,
        GROUP
    }

}

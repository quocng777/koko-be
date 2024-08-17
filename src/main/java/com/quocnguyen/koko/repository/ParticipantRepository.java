package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p " +
            "FROM Participant p " +
            "WHERE p.user.id = ?1 AND p.conservation.id = ?2")
    Optional<Participant> findByUserIdAndConservationId(Long userId, Long conservationId);
}

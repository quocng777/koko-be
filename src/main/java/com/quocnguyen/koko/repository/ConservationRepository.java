package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Conservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Repository
public interface ConservationRepository extends JpaRepository<Conservation, Long> {

    @Query("SELECT c " +
            "FROM Participant p LEFT JOIN Conservation c ON p.conservation.id = c.id " +
            "WHERE p.user.id = ?1")
    Collection<Conservation> findAllByUserId(Long userId);

    @Query("SELECT c " +
            "FROM Conservation c, Participant p " +
            "WHERE c.id = p.conservation.id " +
            "AND c.id = ?1 " +
            "AND p.user.id = ?2")
    Optional<Conservation> findByIdAndUserId(Long consId, Long userId);

}

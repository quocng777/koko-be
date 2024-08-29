package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Conservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT c " +
            "FROM Conservation c JOIN Participant p " +
            "ON (c.id = p.conservation.id AND p.user.id = ?1) " +
            "WHERE (c.type = 'GROUP' AND LOWER(c.name) LIKE LOWER(CONCAT('%', ?2 , '%'))) " +
            "OR (c.type = 'SINGLE' AND EXISTS (" +
            "   SELECT p2 FROM Participant p2 " +
            "   WHERE p2.conservation.id = c.id " +
            "   AND p2.user.id != p.user.id " +
            "   AND LOWER(p2.user.name) LIKE LOWER(CONCAT('%', ?2 , '%')))" +
            ")"
    )
    Page<Conservation> findConservations(Long userId, String keyword, Pageable pageable);
}

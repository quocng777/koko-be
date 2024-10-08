package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Relationship;
import com.quocnguyen.koko.model.RelationshipId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Quoc Nguyen on {2024-08-26}
 */

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {

    @Query("SELECT r FROM Relationship r " +
            "WHERE r.user.id = ?1 " +
            "AND r.type = 'FRIEND' " +
            "AND (?2 IS NULL OR (" +
            "       LOWER(r.relatedUser.username) LIKE LOWER(CONCAT('%', ?2, '%'))  " +
            "       OR LOWER(r.relatedUser.name) LIKE LOWER(CONCAT('%', ?2, '%'))" +
            "       )" +
            "   ) " +
            "AND EXISTS (" +
                "SELECT r2 FROM Relationship r2 " +
                "WHERE r2.user.id = r.relatedUser.id " +
                "AND r2.relatedUser.id = r.user.id " +
                "AND r2.type = 'FRIEND'" +
                ") " +
            "ORDER BY r.createdAt DESC")
    Page<Relationship> getFriends(Long userId, String keyword, Pageable pageable);


    @Query("SELECT r FROM Relationship  r " +
            "WHERE r.relatedUser.id = ?1 " +
            "AND r.type = 'FRIEND' " +
            "AND NOT EXISTS ( " +
            "   SELECT r2 FROM Relationship r2 " +
            "   WHERE r2.user.id = ?1 " +
            "   AND r2.relatedUser.id = r.user.id " +
            "   AND r.type = 'FRIEND' " +
            ") " +
            "ORDER BY r.createdAt DESC ")
    Page<Relationship> getFriendRequests(Long userId, Pageable pageable);

    @Query("SELECT r FROM Relationship r " +
            "WHERE r.user.id = ?1 " +
            "AND  r.relatedUser.id = ?2 " +
            "AND r.type = ?3")
    Optional<Relationship> findByIdAndType(Long userId, Long relatedId, Relationship.RelationshipType type);
}

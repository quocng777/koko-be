package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Relationship;
import com.quocnguyen.koko.model.RelationshipId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Quoc Nguyen on {2024-08-26}
 */

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {

    @Query("SELECT r FROM Relationship r " +
            "WHERE r.user.id = ?1 " +
            "AND r.type = 'FRIEND' " +
            "AND (?2 IS NULL OR (r.relatedUser.name LIKE %?2% OR r.relatedUser.name LIKE %?2%)) " +
            "AND EXISTS (" +
                "SELECT r2 FROM Relationship r2 " +
                "WHERE r2.user.id = r.relatedUser.id " +
                "AND r2.relatedUser.id = r.user.id " +
                "AND r2.type = 'FRIEND'" +
                ") " +
            "ORDER BY r.createdAt DESC")
    Page<Relationship> getFriends(Long userId, String keyword, Pageable pageable);
}

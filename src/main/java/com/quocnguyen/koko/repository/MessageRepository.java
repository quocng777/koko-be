package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE m.conservation.id = ?2 AND m.sender.id = ?1 " +
            "ORDER BY m.createdAt DESC " +
            "LIMIT 1")
    Optional<Message> findLatestMessage(Long userId, Long conservationId);

}

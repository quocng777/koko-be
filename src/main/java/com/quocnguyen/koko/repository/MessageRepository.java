package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.dto.MessageQueryParams;
import com.quocnguyen.koko.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE m.conservation.id = ?1 " +
            "ORDER BY m.createdAt DESC " +
            "LIMIT 1")
    Optional<Message> findLatestMessage(Long conservationId);

    @Query("SELECT m FROM Message m WHERE " +
            "(:#{#params.beforeMessage} IS NULL OR m.id < :#{#params.beforeMessage}) AND " +
            "(:#{#params.conservationId} = m.conservation.id) AND " +
            "(:#{#params.keyword} IS NULL OR m.message LIKE %:#{#params.keyword}%)")
    Page<Message> findAllMessages(@Param("params") MessageQueryParams params, Pageable pageable);
}

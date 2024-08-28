package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {2024-08-27}
 */

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.dismissedAt IS NULL " +
            "AND n.recipient.id = ?1")
    int countNumberUnDismissed(Long userId);


    @Query("SELECT n FROM Notification n " +
            "WHERE n.recipient.id = ?1 " +
            "AND (?2 IS NULL OR n.id < ?2) " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> getNotifications(Long userId, Long beforeId, Pageable pageable);

}

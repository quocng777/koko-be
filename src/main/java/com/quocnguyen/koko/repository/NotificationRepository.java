package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Notification;
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

}

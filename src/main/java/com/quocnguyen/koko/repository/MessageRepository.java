package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}

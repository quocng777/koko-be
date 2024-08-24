package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.SeenMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {2024-08-24}
 */

@Repository
public interface SeenMessageRepository extends CrudRepository<SeenMessage, Long> {
}

package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Attachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Repository
public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
}

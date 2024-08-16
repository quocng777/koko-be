package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.Conservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Repository
public interface ConservationRepository extends JpaRepository<Conservation, Long> {
}

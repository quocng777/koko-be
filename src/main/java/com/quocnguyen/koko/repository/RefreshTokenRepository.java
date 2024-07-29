package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}

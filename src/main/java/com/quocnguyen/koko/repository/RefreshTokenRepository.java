package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.user.id = ?1")
    void deleteByUserId(Long userId);

}

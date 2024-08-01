package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.VerificationCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, Long> {

    @Query("SELECT c FROM VerificationCode c WHERE c.user.id = ?1 AND c.codeType = ?2")
    Optional<VerificationCode> findByUserIdAAndCodeType(Long userId, VerificationCode.CodeType codeType);
}

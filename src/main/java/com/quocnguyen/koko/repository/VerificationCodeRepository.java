package com.quocnguyen.koko.repository;

import com.quocnguyen.koko.model.VerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, Long> {

}

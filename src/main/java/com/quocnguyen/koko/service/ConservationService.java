package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.ConservationRequestParams;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */
public interface ConservationService {
    ConservationDTO create(ConservationRequestParams prams);
}

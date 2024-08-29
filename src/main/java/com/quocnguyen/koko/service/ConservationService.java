package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.ConservationDTO;
import com.quocnguyen.koko.dto.ConservationRequestParams;

import java.util.Collection;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */
public interface ConservationService {
    ConservationDTO create(ConservationRequestParams prams);

    Collection<ConservationDTO> getConservations();

    ConservationDTO get(Long id);

    AppPaging<ConservationDTO> getConservation(int pageNum, int pageSize, String keyword);
}

package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.Conservation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

/**
 * @author Quoc Nguyen on {2024-08-13}
 */

@Data
public class ConservationRequestParams {
    private String name;
    @NotBlank
    private Conservation.ConservationType type;
    private Set<Long> participants;
}

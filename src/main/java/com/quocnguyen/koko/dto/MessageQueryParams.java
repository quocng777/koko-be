package com.quocnguyen.koko.dto;

import lombok.Data;

/**
 * @author Quoc Nguyen on {2024-08-17}
 */

@Data
public class MessageQueryParams {
    private Long beforeMessage;
    private String keyword;
    private Long conservationId;
}

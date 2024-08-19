package com.quocnguyen.koko.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author Quoc Nguyen on {2024-08-19}
 */

@Data
@Builder
public class AttachmentPreSingedUrlDTO {
    private String fileUploadedUrl;
    private String url;
}

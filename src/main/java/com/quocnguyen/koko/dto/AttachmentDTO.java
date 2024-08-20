package com.quocnguyen.koko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String url;
    private String fileName;
    private String fileType;
    private Date createdAt;
}

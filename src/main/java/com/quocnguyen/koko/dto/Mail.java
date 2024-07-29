package com.quocnguyen.koko.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author Quoc Nguyen on {7/29/2024}
 */

@Data
@Builder
public class Mail {
    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mainSubject;
    private String mainContent;
    private boolean isHtml;
    private List<Objects> attachments;
}

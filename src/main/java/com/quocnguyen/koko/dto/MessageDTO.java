package com.quocnguyen.koko.dto;

import com.quocnguyen.koko.model.Attachment;
import com.quocnguyen.koko.model.Conservation;
import com.quocnguyen.koko.model.Message;
import com.quocnguyen.koko.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;

    @NotNull
    private Long conservation;

    @NotNull
    private Long sender;

    private String message;


    private Date createdAt;


    private Message.MessageType type;

    private Set<AttachmentDTO> attachments;

    public static MessageDTO convert(Message message) {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .id(message.getId())
                .message(message.getMessage())
                .type(message.getType())
                .attachments(message.getAttachments() == null
                                ? null
                                : message.getAttachments()
                                .stream()
                                .map(atc -> new AttachmentDTO(atc.getId(), atc.getUrl(), atc.getCreatedAt()))
                                .collect(Collectors.toSet())
                        )
                .sender(message.getSender().getId())
                .build();

        return messageDTO;
    }
}

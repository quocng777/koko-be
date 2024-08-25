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

    private Long sender;

    private String message;


    private Date createdAt;


    private Message.MessageType type;

    private Set<AttachmentDTO> attachments;
    private Set<MessageSeenDTO> seenBy;

    public static MessageDTO convert(Message message) {
        MessageDTO messageDTO = MessageDTO
                .builder()
                .id(message.getId())
                .conservation(message.getConservation().getId())
                .message(message.getMessage())
                .type(message.getType())
                .attachments(message.getAttachments() == null
                                ? null
                                : message.getAttachments()
                                .stream()
                                .map(atc -> AttachmentDTO
                                        .builder()
                                        .id(atc.getId())
                                        .url(atc.getUrl())
                                        .fileName(atc.getFileName())
                                        .fileType(atc.getFileType())
                                        .createdAt(atc.getCreatedAt())
                                        .build())
                                .collect(Collectors.toSet())
                        )
                .seenBy(
                        message.getSeenUser() == null
                        ? null : message
                                .getSeenUser()
                                .stream()
                                .map((seenMsg -> {
                                    return MessageSeenDTO
                                            .builder()
                                            .user(seenMsg.getUser().getId())
                                            .seenAt(seenMsg.getSeenAt())
                                            .build();
                                }))
                                .collect(Collectors.toSet())
                )
                .sender(message.getSender().getId())
                .createdAt(message.getCreatedAt())
                .build();

        return messageDTO;
    }
}

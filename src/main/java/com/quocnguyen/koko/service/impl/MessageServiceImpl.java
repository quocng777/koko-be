package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.Attachment;
import com.quocnguyen.koko.model.Conservation;
import com.quocnguyen.koko.model.Message;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.AttachmentRepository;
import com.quocnguyen.koko.repository.ConservationRepository;
import com.quocnguyen.koko.repository.MessageRepository;
import com.quocnguyen.koko.service.MessageService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final ConservationRepository conservationRepository;
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;


    @Override
    @Transactional
    public MessageDTO create(MessageDTO receivedMsg) {
        UserDTO user = userService.getAuthenticatedUser();

        Conservation conservation = conservationRepository
                .findById(receivedMsg.getConservation())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Message message = Message
                .builder()
                .sender(new User(user.getId()))
                .createdAt(new Date())
                .type(receivedMsg.getType())
                .conservation(conservation)
                .build();

        if(message.getType() == Message.MessageType.TEXT) {
            message.setMessage(receivedMsg.getMessage());
        } else {
            Set<Attachment> attachments = receivedMsg
                    .getAttachments()
                    .stream()
                    .map((atc) -> {
                        return attachmentRepository
                                .findById(atc.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(String.format("Attachment %s not found", atc.getId())));
                    })
                    .collect(Collectors.toSet());
        }

        Message savedMessage = messageRepository.save(message);

        return MessageDTO.convert(message);
    }
}

package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.dto.MessageQueryParams;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.event.MessageSendEvent;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.Attachment;
import com.quocnguyen.koko.model.Conservation;
import com.quocnguyen.koko.model.Message;
import com.quocnguyen.koko.model.User;
import com.quocnguyen.koko.repository.AttachmentRepository;
import com.quocnguyen.koko.repository.ConservationRepository;
import com.quocnguyen.koko.repository.MessageRepository;
import com.quocnguyen.koko.repository.ParticipantRepository;
import com.quocnguyen.koko.service.MessageService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ApplicationEventPublisher eventPublisher;
    private final ParticipantRepository participantRepository;


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
        }
        Message savedMessage = messageRepository.save(message);

        Set<Attachment> attachments = receivedMsg
                .getAttachments()
                .stream()
                .map((atc) -> {
                    Attachment attachment = Attachment
                            .builder()
                            .fileName(atc.getFileName())
                            .url(atc.getUrl())
                            .message(savedMessage)
                            .createdAt(new Date())
                            .build();
                    var savedAttachment = attachmentRepository.save(attachment);

                    return savedAttachment;
                })
                .collect(Collectors.toSet());

        savedMessage.setAttachments(attachments);
        var returnMessage = MessageDTO.convert(message);

        eventPublisher.publishEvent(new MessageSendEvent(this, returnMessage, conservation));

        return returnMessage;
    }

    @Override
    public MessageDTO getLatestMessage(Long conservation) {
        var user = userService.getAuthenticatedUser();

        participantRepository.findByUserIdAndConservationId(user.getId(), conservation)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Conservation %d not found", conservation)));

        Message message = messageRepository.findLatestMessage(conservation)
                .orElse(null);

        if(message == null)
            return null;

        return MessageDTO.convert(message);
    }

    @Override
    public AppPaging<MessageDTO> getMessages(MessageQueryParams params, Integer pageSize, Integer pageNum) {
        var user = userService.getAuthenticatedUser();
        if(params.getConservationId() != null) {
            participantRepository.findByUserIdAndConservationId(user.getId(), params.getConservationId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Conservation %d not found", params.getConservationId())));
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Message> result = messageRepository.findAllMessages(params, pageable);

        var list = result.getContent().stream().map(MessageDTO::convert).collect(Collectors.toCollection(ArrayList::new));

        AppPaging<MessageDTO> page =  AppPaging.convertExcludeContent(result);
        page.setList(list);

        return page;
    }
}

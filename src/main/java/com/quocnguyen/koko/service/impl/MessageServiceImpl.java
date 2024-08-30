package com.quocnguyen.koko.service.impl;

import com.quocnguyen.koko.dto.*;
import com.quocnguyen.koko.event.MessageDeleteEvent;
import com.quocnguyen.koko.event.MessageSeenEvent;
import com.quocnguyen.koko.event.MessageSendEvent;
import com.quocnguyen.koko.exception.ResourceNotFoundException;
import com.quocnguyen.koko.model.*;
import com.quocnguyen.koko.repository.*;
import com.quocnguyen.koko.service.FileService;
import com.quocnguyen.koko.service.MessageService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final ConservationRepository conservationRepository;
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ParticipantRepository participantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final SeenMessageRepository seenMessageRepository;
    private final FileService fileService;


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
        } else if (message.getType() == Message.MessageType.BROADCAST) {
            BroadCastInfo broadCastInfo = new BroadCastInfo();
            broadCastInfo.setBroadCastType(receivedMsg.getBroadcast().getBroadcastType());

            if(receivedMsg.getBroadcast().getTargetUsers() != null) {
                broadCastInfo.setTargetUsers(
                        receivedMsg.getBroadcast().getTargetUsers()
                                .stream()
                                .map(mem -> new User(mem.getId()))
                                .collect(Collectors.toSet())
                );
            } else {
                broadCastInfo.setTargetUsers(new HashSet<>());
            }

            message.setBroadCastInfo(broadCastInfo);
        }

        Message savedMessage = messageRepository.save(message);

        if(receivedMsg.getAttachments() != null) {
            Set<Attachment> attachments = receivedMsg
                    .getAttachments()
                    .stream()
                    .map((atc) -> {
                        Attachment attachment = Attachment
                                .builder()
                                .fileName(atc.getFileName())
                                .url(atc.getUrl())
                                .message(savedMessage)
                                .fileType(atc.getFileType())
                                .createdAt(new Date())
                                .build();

                        return attachmentRepository.save(attachment);
                    })
                    .collect(Collectors.toSet());

            savedMessage.setAttachments(attachments);
        }
        var returnMessage = MessageDTO.convert(message);

        System.out.println("PUBLISH EVENT");
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

    @Override
    public void sendIsTyping(Principal principal, MessageTyping messageTyping) {
        String username = principal.getName();
        if(username == null)
            return;

        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null)
            return;

        conservationRepository
                .findByIdAndUserId(messageTyping.getConservation(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Conservation not found"));

        messageTyping.setUser(user.getId());

        // resend to users in the group
        System.out.println("/typing/" + messageTyping.getConservation());
        messagingTemplate.convertAndSend("/messages/typing/" + messageTyping.getConservation(), messageTyping);
    }

    @Override
    public MessageSeenDTO updateSeenStatus(Long conservationId) {
        var user = userService.getAuthenticatedUser();
        var conservation = conservationRepository.findById(conservationId).orElse(null);

        if(conservation == null)
            return null;

        // get unread messages
        var messages = messageRepository.findUnreadMessages(conservationId, user.getId());

        if(messages.size() == 0)
            return null;

        // crete SeenMessage objects
        List<SeenMessage> seenMessages = messages
                .stream()
                .map(message -> {
                    return SeenMessage
                            .builder()
                            .message(message)
                            .user(new User(user.getId()))
                            .seenAt(new Date())
                            .build();
                })
                .toList();

        seenMessageRepository.saveAll(seenMessages);

        SeenMessage lastSeen = seenMessages.get(seenMessages.size() - 1);

        MessageSeenDTO dto = MessageSeenDTO
                .builder()
                .user(lastSeen.getUser().getId())
                .message(lastSeen.getMessage().getId())
                .conservation(conservationId)
                .seenAt(lastSeen.getSeenAt())
                .build();

        eventPublisher.publishEvent(new MessageSeenEvent(this, dto, conservation));

        return dto;
    }

    @Override
    public int getNumUnreadMessage(Long conservationId) {
        var user = userService.getAuthenticatedUser();

        return messageRepository.countUnreadMessages(conservationId, user.getId());
    }

    @Override
    @Transactional
    public MessageDTO deleteMessage(Long messageId) {
        var user = userService.getAuthenticatedUser();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if(!message.getSender().getId().equals(user.getId()))
            throw new ResourceNotFoundException("Message not found");

        if(message.getDeletedAt() != null)
            return null;

        if(message.getType() == Message.MessageType.TEXT) {
            message.setMessage(null);
        }

        // delete all attachment from s3
        if( message.getAttachments().size() > 0 ) {
            String[] objectKeys = message
                    .getAttachments()
                    .stream()
                    .map(Attachment::getKeyObject)
                    .toArray(String[]::new);
            attachmentRepository.deleteAll(message.getAttachments());
            message.setAttachments(null);
            fileService.deleteFiles(objectKeys);
        }

        message.setDeletedAt(new Date());
        message.setType(Message.MessageType.DELETED);

        message = messageRepository.save(message);

        var dto = MessageDTO.convert(message);

        eventPublisher.publishEvent(new MessageDeleteEvent(this, dto, message.getConservation()));

        return dto;
    }
}

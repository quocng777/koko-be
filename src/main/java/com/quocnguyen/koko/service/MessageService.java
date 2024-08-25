package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.*;

import java.security.Principal;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */
public interface MessageService {

    MessageDTO create(MessageDTO receivedMsg);

    MessageDTO getLatestMessage(Long conservation);

    AppPaging<MessageDTO> getMessages(MessageQueryParams params,
                                      Integer pageSize,
                                      Integer pageNum);

    void sendIsTyping(Principal principal, MessageTyping messageTyping);

    MessageSeenDTO updateSeenStatus(Long conservationId);

    int  getNumUnreadMessage(Long conservationId);
}

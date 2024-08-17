package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.AppPaging;
import com.quocnguyen.koko.dto.MessageDTO;
import com.quocnguyen.koko.dto.MessageQueryParams;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */
public interface MessageService {

    MessageDTO create(MessageDTO receivedMsg);

    MessageDTO getLatestMessage(Long conservation);

    AppPaging<MessageDTO> getMessages(MessageQueryParams params,
                                      Integer pageSize,
                                      Integer pageNum);
}

package com.quocnguyen.koko.service;

import com.quocnguyen.koko.dto.MessageDTO;

/**
 * @author Quoc Nguyen on {2024-08-16}
 */
public interface MessageService {

    MessageDTO create(MessageDTO receivedMsg);
}

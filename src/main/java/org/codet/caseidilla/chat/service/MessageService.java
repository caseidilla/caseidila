package org.codet.caseidilla.chat.service;

import java.util.List;

import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.dto.SendMessageRequest;

public interface MessageService {
    void sendMessage(SendMessageRequest sendMessageRequest);

    List<MessageDto> listDialogMessages(String participant, String user);
}

package org.codet.caseidilla.chat.service;

import org.codet.caseidilla.chat.dto.IncomingMessageDto;
import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.dto.SendMessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> listDialogMessages(String participant, String user);

    SendMessageDto sendMessage(IncomingMessageDto incomingMessage);
}

package org.codet.caseidilla.chat.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.dto.SendMessageRequest;
import org.codet.caseidilla.chat.entity.Message;
import org.codet.caseidilla.chat.repository.MessageRepository;
import org.codet.caseidilla.chat.service.MessageService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest) {
    }

    @Override
    public List<MessageDto> listDialogMessages(String participant, String user) {
        List<Message> sentMessages = messageRepository.findAllBySenderAndReceiver(user, participant);
        List<Message> receiveMessages = messageRepository.findAllBySenderAndReceiver(participant, user);
        return Stream
                .concat(
                        sentMessages.stream()
                                .map(message -> MessageDto.builder()
                                        .body(message.getBody())
                                        .timestamp(message.getTimestamp())
                                        .type(MessageDto.MessageType.SENT)
                                        .build()),
                        receiveMessages.stream()
                                .map(message -> MessageDto.builder()
                                        .body(message.getBody())
                                        .timestamp(message.getTimestamp())
                                        .type(MessageDto.MessageType.RECEIVED)
                                        .build())
                )
                .collect(Collectors.toList());
    }
}

package org.codet.caseidilla.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.codet.caseidilla.chat.dto.IncomingMessageDto;
import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.dto.SendMessageDto;
import org.codet.caseidilla.chat.entity.Message;
import org.codet.caseidilla.chat.repository.MessageRepository;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.chat.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final DialogService dialogService;
    private final MessageRepository messageRepository;

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
                .sorted(Comparator.comparing(MessageDto::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SendMessageDto sendMessage(IncomingMessageDto incomingMessage) {
        String to = incomingMessage.getTo();
        String from = incomingMessage.getFrom();
        Instant timestamp = Instant.now();
        dialogService.findAndValidateDialog(from, to);
        Message message = Message.builder()
                .receiver(to)
                .sender(from)
                .body(incomingMessage.getBody())
                .timestamp(timestamp)
                .build();
        messageRepository.save(message);
        return SendMessageDto.builder()
                .body(incomingMessage.getBody())
                .from(from)
                .timestamp(timestamp)
                .build();
    }
}

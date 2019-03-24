package org.codet.caseidilla.unit.chat.service;

import com.google.common.collect.ImmutableList;
import org.codet.caseidilla.chat.dto.IncomingMessageDto;
import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.dto.SendMessageDto;
import org.codet.caseidilla.chat.entity.Message;
import org.codet.caseidilla.chat.repository.MessageRepository;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.chat.service.impl.MessageServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    @Mock
    private DialogService dialogService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    public void shouldListDialogMessages() {
        // given
        doReturn(ImmutableList.of(
                Message.builder()
                        .body("Hi")
                        .receiver("loupa")
                        .sender("poupa")
                        .timestamp(Instant.ofEpochMilli(1))
                        .build(),
                Message.builder()
                        .body("Loupa")
                        .receiver("loupa")
                        .sender("poupa")
                        .timestamp(Instant.ofEpochMilli(3))
                        .build()
        )).when(messageRepository).findAllBySenderAndReceiver("poupa", "loupa");
        doReturn(ImmutableList.of(
                Message.builder()
                        .body("Hi")
                        .receiver("poupa")
                        .sender("loupa")
                        .timestamp(Instant.ofEpochMilli(2))
                        .build(),
                Message.builder()
                        .body("Poupa")
                        .receiver("poupa")
                        .sender("loupa")
                        .timestamp(Instant.ofEpochMilli(4))
                        .build()
        )).when(messageRepository).findAllBySenderAndReceiver("loupa", "poupa");

        // when
        List<MessageDto> actual = messageService.listDialogMessages("poupa", "loupa");

        // then
        assertThat(actual, hasItems(
                MessageDto.builder()
                        .body("Hi")
                        .type(MessageDto.MessageType.RECEIVED)
                        .timestamp(Instant.ofEpochMilli(1))
                        .build(),
                MessageDto.builder()
                        .body("Hi")
                        .type(MessageDto.MessageType.SENT)
                        .timestamp(Instant.ofEpochMilli(2))
                        .build(),
                MessageDto.builder()
                        .body("Loupa")
                        .type(MessageDto.MessageType.RECEIVED)
                        .timestamp(Instant.ofEpochMilli(3))
                        .build(),
                MessageDto.builder()
                        .body("Poupa")
                        .type(MessageDto.MessageType.SENT)
                        .timestamp(Instant.ofEpochMilli(4))
                        .build()
        ));
    }

    @Test
    public void shouldSendMessage() {
        // given
        // when
        SendMessageDto actual = messageService.sendMessage(IncomingMessageDto.builder()
                .from("poupa")
                .to("loupa")
                .body("give me my salary")
                .build());

        // then
        verify(dialogService, times(1)).findAndValidateDialog("poupa", "loupa");
        verify(messageRepository, times(1)).save(Message.builder()
                .sender("poupa")
                .receiver("loupa")
                .timestamp(any())
                .body("give me my salary")
                .build());
        assertThat(actual, hasProperty("from", is("poupa")));
        assertThat(actual, hasProperty("body", is("give me my salary")));
   }
}

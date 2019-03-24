package org.codet.caseidilla.unit.chat.rest;

import com.google.common.collect.ImmutableList;
import org.codet.caseidilla.chat.dto.ChangeDialogNameRequestDto;
import org.codet.caseidilla.chat.dto.DialogDto;
import org.codet.caseidilla.chat.dto.IncomingMessageDto;
import org.codet.caseidilla.chat.dto.SendMessageRequestDto;
import org.codet.caseidilla.chat.rest.ChatController;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.chat.service.MessageService;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatControllerTest {

    @Mock
    private DialogService dialogService;
    @Mock
    private MessageService messageService;


    @InjectMocks
    private ChatController chatController;

    @Test
    public void shouldListDialogs() {
        // given
        doReturn(ImmutableList.of(
                DialogDto.builder()
                        .build()))
                .when(dialogService).listDialogs("loupa");

        // when
        List<DialogDto> actual = chatController.listDialogs("loupa");

        // then
        assertThat(actual, hasItems(DialogDto.builder().build()));
    }

    @Test
    public void shouldListHiddenDialogs() {
        // given
        doReturn(ImmutableList.of(
                DialogDto.builder()
                        .build()))
                .when(dialogService).listHiddenDialogs(PinDto.builder().build(), "loupa");

        // when
        List<DialogDto> actual = chatController.listHiddenDialogs(PinDto.builder().build(), "loupa");

        // then
        assertThat(actual, hasItems(DialogDto.builder().build()));
    }

    @Test
    public void shouldSendMessage() {
        // given
        // when
        chatController.sendMessage(SendMessageRequestDto.builder()
                        .body("hi")
                        .participant("poupa")
                        .build(),
                "loupa");

        // then
        verify(messageService, times(1)).sendMessage(
                IncomingMessageDto.builder()
                        .body("hi")
                        .to("poupa")
                        .from("loupa")
                        .build());
    }

    @Test
    public void shouldChangeName() {
        // given
        // when
        chatController.changeDialogName(ChangeDialogNameRequestDto.builder()
                        .participant("poupa")
                        .name("name")
                        .build(),
                "loupa");
        // then
        verify(dialogService, times(1))
                .changeName(ChangeDialogNameRequestDto.builder()
                                .name("name")
                                .participant("poupa")
                                .build(),
                        "loupa");
    }
}

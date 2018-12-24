package org.codet.caseidilla.chat.rest;

import lombok.RequiredArgsConstructor;
import org.codet.caseidilla.chat.dto.*;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.chat.service.MessageService;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final DialogService dialogService;
    private final MessageService messageService;

    @GetMapping("/{login}/dialogs")
    public List<DialogDto> listDialogs(@PathVariable String login) {
        return dialogService.listDialogs(login);
    }

    @PostMapping("/{login}/dialogs/hidden")
    public List<DialogDto> listHiddenDialogs(@RequestBody PinDto request, @PathVariable String login) {
        return dialogService.listHiddenDialogs(request, login);
    }

    @GetMapping("/{login}/dialog")
    public List<MessageDto> getDialog(@RequestParam String participant, @PathVariable String login) {
        return messageService.listDialogMessages(participant, login);
    }

    @PostMapping("/{login}/dialog/send")
    public void sendMessage(@RequestBody SendMessageRequestDto sendMessage, @PathVariable String login) {
        messageService.sendMessage(IncomingMessageDto.builder()
                .body(sendMessage.getBody())
                .to(sendMessage.getParticipant())
                .from(login)
                .build());
    }

    @PostMapping("/{login}/dialog/changeName")
    public void changeDialogName(@RequestBody ChangeDialogNameRequestDto request, @PathVariable String login) {
        dialogService.changeName(request, login);
    }

    @PostMapping("/{login}/dialog/hide")
    public void hideDialog(@RequestBody HideDialogRequestDto request, @PathVariable String login) {
        dialogService.hideDialog(request, login);
    }

    @PostMapping("/{login}/dialog/new")
    public void createDialog(@RequestBody NewDialogDto request, @PathVariable String login) {
        dialogService.createDialog(request, login);
    }

    @PostMapping("/{login}/dialog/delete")
    public void deleteDialog(@RequestBody DeleteDialogDto request, @PathVariable String login) {
        dialogService.deleteDialog(request, login);
    }

    @GetMapping("/{login}/find")
    public FindDialogResponseDto findDialog(@RequestParam String participant, @PathVariable String login) {
        return dialogService.findDialog(participant, login);
    }
}


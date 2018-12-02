package org.codet.caseidilla.chat.rest;

import java.util.List;

import org.codet.caseidilla.chat.dto.ChangeDialogNameRequestDto;
import org.codet.caseidilla.chat.dto.DialogDto;
import org.codet.caseidilla.chat.dto.HideDialogRequestDto;
import org.codet.caseidilla.chat.dto.MessageDto;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.chat.service.MessageService;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
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
    public void sendMessage() {
        throw new UnsupportedOperationException("Messengers does not do this kind of things");
    }

    @PostMapping("/{login}/dialog/changeName")
    public void changeDialogName(@RequestBody ChangeDialogNameRequestDto request, @PathVariable String login) {
        dialogService.changeName(request, login);
    }

    @PostMapping("/{login}/dialog/hide")
    public void hideDialog(@RequestBody HideDialogRequestDto request, @PathVariable String login) {
        dialogService.hideDialog(request, login);
    }

}


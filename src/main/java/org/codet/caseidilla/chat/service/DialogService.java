package org.codet.caseidilla.chat.service;

import java.util.List;

import org.codet.caseidilla.chat.dto.ChangeDialogNameRequestDto;
import org.codet.caseidilla.chat.dto.DialogDto;
import org.codet.caseidilla.chat.dto.HideDialogRequestDto;
import org.codet.caseidilla.user.credentials.dto.PinDto;

public interface DialogService {

    List<DialogDto> listDialogs(String user);

    List<DialogDto> listHiddenDialogs(PinDto request, String login);

    void changeName(ChangeDialogNameRequestDto request, String login);

    void hideDialog(HideDialogRequestDto request, String login);
}

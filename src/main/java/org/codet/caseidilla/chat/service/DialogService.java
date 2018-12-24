package org.codet.caseidilla.chat.service;

import org.codet.caseidilla.chat.dto.*;
import org.codet.caseidilla.chat.entity.Dialog;
import org.codet.caseidilla.user.credentials.dto.PinDto;

import java.util.List;

public interface DialogService {

    List<DialogDto> listDialogs(String user);

    List<DialogDto> listHiddenDialogs(PinDto request, String login);

    void changeName(ChangeDialogNameRequestDto request, String login);

    void hideDialog(HideDialogRequestDto request, String login);

    Dialog findAndValidateDialog(String login, String participant);

    void createDialog(NewDialogDto request, String login);

    void deleteDialog(DeleteDialogDto request, String login);

    FindDialogResponseDto findDialog(String participant, String login);
}

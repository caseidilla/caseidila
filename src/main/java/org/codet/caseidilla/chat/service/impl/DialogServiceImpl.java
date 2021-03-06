package org.codet.caseidilla.chat.service.impl;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import org.codet.caseidilla.chat.dto.*;
import org.codet.caseidilla.chat.entity.Dialog;
import org.codet.caseidilla.chat.repository.DialogRepository;
import org.codet.caseidilla.chat.service.DialogService;
import org.codet.caseidilla.exception.CaseidillaException;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogServiceImpl implements DialogService {

    private final UserRepository userRepository;
    private final DialogRepository dialogRepository;

    @Override
    public List<DialogDto> listDialogs(String login) {
        Set<Dialog> dialogs = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"))
                .getDialogs();
        return dialogs.stream()
                .filter(dialog -> !dialog.isHidden())
                .map(dialog -> toDialogDto(login, dialog))
                .collect(Collectors.toList());
    }

    @Override
    public List<DialogDto> listHiddenDialogs(PinDto request, String login) {
        User user = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"));
        if (user.getPin() == null) {
            throw new CaseidillaException("Pin is not set up");
        }
        if (!user.getPin().equals(request.getPin())) {
            throw new CaseidillaException("Wrong pin");
        }
        Set<Dialog> dialogs = user.getDialogs();
        return dialogs.stream()
                .map(dialog -> toDialogDto(login, dialog))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changeName(ChangeDialogNameRequestDto request, String login) {
        Dialog dialog = findAndValidateDialog(login, request.getParticipant());
        dialog.setName(request.getName());
    }

    @Override
    @Transactional
    public void hideDialog(HideDialogRequestDto request, String login) {
        User user = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"));
        if (user.getPin() == null) {
            throw new CaseidillaException("No pin");
        }
        if (!request.getPin().equals(user.getPin())) {
            throw new CaseidillaException("Wrong pin");
        }
        Dialog dialog = findAndValidateDialog(login, request.getParticipant());
        dialog.setHidden(true);
    }

    @Override
    public Dialog findAndValidateDialog(String login, String participant) {
        Set<Dialog> dialogs = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"))
                .getDialogs();
        User participantUser = userRepository.findById(participant)
                .orElseThrow(() -> new CaseidillaException("Can't find participant"));
        return dialogs.stream()
                .filter(dialog -> dialog.getUsers().contains(participantUser))
                .findFirst()
                .orElseThrow(() -> new CaseidillaException("There is no dialog with this users"));
    }

    @Override
    @Transactional
    public void createDialog(NewDialogDto request, String login) {
        User user = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"));
        User participantUser = userRepository.findById(request.getParticipant())
                .orElseThrow(() -> new CaseidillaException("Can't find participant"));
        boolean isDialogExists = user.getDialogs()
                .stream()
                .anyMatch(dialog -> dialog.getUsers().contains(participantUser));
        if (isDialogExists) {
            throw new CaseidillaException("Dialog already exists");
        }

        Dialog newDialog = Dialog.builder()
                .hidden(false)
                .secret(request.isSecret())
                .users(ImmutableSet.of(user, participantUser))
                .build();
        newDialog = dialogRepository.save(newDialog);
        user.getDialogs().add(newDialog);
        participantUser.getDialogs().add(newDialog);
    }

    @Override
    @Transactional
    public void deleteDialog(DeleteDialogDto request, String login) {
        User user = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"));
        User participantUser = userRepository.findById(request.getParticipant())
                .orElseThrow(() -> new CaseidillaException("Can't find participant"));
        Dialog foundDialog = user.getDialogs()
                .stream()
                .filter(dialog -> dialog.getUsers().contains(participantUser))
                .findFirst()
                .orElseThrow(() -> new CaseidillaException("Dialog is not found"));
        user.getDialogs().remove(foundDialog);
        participantUser.getDialogs().remove(foundDialog);
        dialogRepository.delete(foundDialog);
    }

    @Override
    @Transactional
    public FindDialogResponseDto findDialog(String participant, String login) {
        return FindDialogResponseDto.builder()
                .found(userRepository.findById(participant).isPresent())
                .build();
    }

    private DialogDto toDialogDto(String login, Dialog dialog) {
        Set<User> users = dialog.getUsers();
        if (users.size() != 2) {
            throw new CaseidillaException("Dialog does not have 2 participants");
        }
        User participant = users.stream()
                .filter(u -> !u.getLogin().equals(login))
                .findFirst()
                .get();
        String dialogName = dialog.getName();
        return DialogDto.builder()
                .login(participant.getLogin())
                .name(dialogName == null ? participant.getLogin() : dialogName)
                .secret(dialog.isSecret())
                .hidden(dialog.isHidden())
                .build();
    }
}

package org.codet.caseidilla.unit.chat.service;

import com.google.common.collect.ImmutableSet;
import org.codet.caseidilla.chat.dto.*;
import org.codet.caseidilla.chat.entity.Dialog;
import org.codet.caseidilla.chat.repository.DialogRepository;
import org.codet.caseidilla.chat.service.impl.DialogServiceImpl;
import org.codet.caseidilla.exception.CaseidillaException;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DialogServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Mock
    private UserRepository userRepository;
    @Mock
    private DialogRepository dialogRepository;
    @Mock
    private User user;
    @Mock
    private User participant;
    @Mock
    private Dialog dialog;

    @InjectMocks
    private DialogServiceImpl dialogService;

    @Test
    public void shuoldListDialogs() {
        // given
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build(),
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("alex")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(true)
                                .hidden(false)
                                .id(2)
                                .name("name")
                                .build(),
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("sveta")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(true)
                                .id(3)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");

        // when
        List<DialogDto> actual = dialogService.listDialogs("loupa");

        // then
        assertThat(actual, contains(
                DialogDto.builder()
                        .login("poupa")
                        .name("poupa")
                        .hidden(false)
                        .secret(false)
                        .build(),
                DialogDto.builder()
                        .login("alex")
                        .name("name")
                        .hidden(false)
                        .secret(true)
                        .build()
        ));
        assertThat(actual, hasSize(2));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionOnListDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");
        // when
        dialogService.listDialogs("loupa");

        // then
    }

    @Test
    public void shouldThrowNotEnoughDialogsExceptionOnListDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Dialog does not have 2 participants");
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");
        // when
        dialogService.listDialogs("loupa");

        // then
    }

    @Test
    public void shuoldListHiddenDialogs() {
        // given
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build(),
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("alex")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(true)
                                .hidden(false)
                                .id(2)
                                .name("name")
                                .build(),
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("sveta")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(true)
                                .id(3)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");

        // when
        List<DialogDto> actual = dialogService.listHiddenDialogs(PinDto.builder()
                        .pin("22")
                        .build(),
                "loupa");

        // then
        assertThat(actual, contains(
                DialogDto.builder()
                        .login("poupa")
                        .name("poupa")
                        .hidden(false)
                        .secret(false)
                        .build(),
                DialogDto.builder()
                        .login("alex")
                        .name("name")
                        .hidden(false)
                        .secret(true)
                        .build(),
                DialogDto.builder()
                        .login("sveta")
                        .name("sveta")
                        .hidden(true)
                        .secret(false)
                        .build()
        ));
        assertThat(actual, hasSize(3));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionOnListHiddenDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");
        // when
        dialogService.listHiddenDialogs(PinDto.builder()
                        .pin("22")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowPinIsNotSetUpExceptionOnListHiddenDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Pin is not set up");
        doReturn(Optional.of(User.builder()
                .pin(null)
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");
        // when
        dialogService.listHiddenDialogs(PinDto.builder()
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowWrongPinExceptionOnListHiddenDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Wrong pin");
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build(),
                                        User.builder()
                                                .login("loupa")
                                                .password("2")
                                                .pin("22")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");
        // when
        dialogService.listHiddenDialogs(PinDto.builder()
                        .pin("12")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowNotEnoughDialogsExceptionOnListHiddenDialogs() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Dialog does not have 2 participants");
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(
                                        User.builder()
                                                .login("poupa")
                                                .password("1")
                                                .pin("11")
                                                .build()
                                ))
                                .secret(false)
                                .hidden(false)
                                .id(1)
                                .name(null)
                                .build()))
                .build()
        )).when(userRepository).findById("loupa");
        // when
        dialogService.listHiddenDialogs(PinDto.builder()
                        .pin("22")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldChangeName() {
        // given
        doReturn(Optional.of(user))
                .when(userRepository).findById("loupa");
        doReturn(ImmutableSet.of(dialog))
                .when(user).getDialogs();
        doReturn(ImmutableSet.of(participant))
                .when(dialog).getUsers();

        doReturn(Optional.of(participant))
                .when(userRepository).findById("poupa");

        // when
        dialogService.changeName(ChangeDialogNameRequestDto.builder()
                        .name("dialog")
                        .participant("poupa")
                        .build(),
                "loupa");

        // then
        verify(dialog, times(1)).setName("dialog");
    }

    @Test
    public void shouldHideDialog() {
        // given
        doReturn(Optional.of(user))
                .when(userRepository).findById("loupa");
        doReturn(ImmutableSet.of(dialog))
                .when(user).getDialogs();
        doReturn("111")
                .when(user).getPin();
        doReturn(ImmutableSet.of(participant))
                .when(dialog).getUsers();

        doReturn(Optional.of(participant))
                .when(userRepository).findById("poupa");

        // when
        dialogService.hideDialog(HideDialogRequestDto.builder()
                        .participant("poupa")
                        .pin("111")
                        .build(),
                "loupa");

        // then
        verify(dialog, times(1)).setHidden(true);
    }

    @Test
    public void shouldThrowUserNotFoundOnHideDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");

        // when
        dialogService.hideDialog(HideDialogRequestDto.builder()
                        .participant("poupa")
                        .pin("111")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldFindAndValidateDialog() {
        // given
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("poupa")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        foundDialog
                ))
                .build()
        )).when(userRepository).findById("loupa");

        doReturn(Optional.of(User.builder()
                .login("poupa")
                .password("1")
                .pin("11")
                .build()))
                .when(userRepository).findById("poupa");

        // when
        Dialog actual = dialogService.findAndValidateDialog("loupa", "poupa");

        // then
        assertThat(actual, is(foundDialog));
    }

    @Test
    public void shouldThrowUserNotFoundOnFindAndValidateDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");

        // when
        dialogService.findAndValidateDialog("loupa", "poupa");
        // then
    }

    @Test
    public void shouldThrowCantFindParticipantExceptionOnFindAndValidateDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Can't find participant");
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("poupa")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        foundDialog
                ))
                .build()
        )).when(userRepository).findById("loupa");

        // when
        dialogService.findAndValidateDialog("loupa", "poupa");

        // then
    }

    @Test
    public void shouldThrownCantFindDialogOnFindAndValidateDialog() {
        // given
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("a")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        doReturn(Optional.of(User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        foundDialog
                ))
                .build()
        )).when(userRepository).findById("loupa");

        doReturn(Optional.of(User.builder()
                .login("poupa")
                .password("1")
                .pin("11")
                .build()))
                .when(userRepository).findById("poupa");
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("There is no dialog with this users");

        // when
        dialogService.findAndValidateDialog("loupa", "poupa");

        // then
    }

    @Test
    public void shouldCreateDialog() {
        // given
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>())
                .build();
        User poupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>())
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");
        doReturn(Optional.of(poupa))
                .when(userRepository).findById("poupa");

        // when
        dialogService.createDialog(NewDialogDto.builder()
                        .participant("poupa")
                        .secret(false)
                        .build(),
                "loupa");

        // then
        verify(dialogRepository)
                .save(Dialog.builder()
                        .id(0)
                        .name(null)
                        .hidden(false)
                        .secret(false)
                        .users(ImmutableSet.of(poupa, loupa))
                        .build());
    }

    @Test
    public void shouldThrowUserNotFoundOnCreateDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");

        // when
        dialogService.createDialog(NewDialogDto.builder()
                        .participant("poupa")
                        .secret(false)
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowCantFindParticipantOnCreateDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Can't find participant");
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>())
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");


        // when
        dialogService.createDialog(NewDialogDto.builder()
                        .participant("poupa")
                        .secret(false)
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowDialogAlreadyExistsOnCreateDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Dialog already exists");
        User poupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>())
                .build();
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(ImmutableSet.of(
                        Dialog.builder()
                                .users(ImmutableSet.of(poupa))
                                .build()
                ))
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");
        doReturn(Optional.of(poupa))
                .when(userRepository).findById("poupa");

        // when
        dialogService.createDialog(NewDialogDto.builder()
                        .participant("poupa")
                        .secret(false)
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldDeleteDialog() {
        // given
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("poupa")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>(Collections.singletonList(foundDialog)))
                .build();
        User poupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>(Collections.singletonList(foundDialog)))
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");
        doReturn(Optional.of(poupa))
                .when(userRepository).findById("poupa");

        // when
        dialogService.deleteDialog(DeleteDialogDto.builder()
                        .participant("poupa")
                        .build(),
                "loupa");

        // then
        verify(dialogRepository).delete(foundDialog);
    }

    @Test
    public void shouldThrowUserNotFoundOnDeleteDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("User not found");

        // when
        dialogService.deleteDialog(DeleteDialogDto.builder()
                        .participant("poupa")
                        .build(),
                "loupa");
    }

    @Test
    public void shouldThrowCanFindParticipantOnDeleteDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Can't find participant");
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("poupa")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>(Collections.singletonList(foundDialog)))
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");


        // when
        dialogService.deleteDialog(DeleteDialogDto.builder()
                        .participant("poupa")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldThrowDialogIsNotFoundOnDeleteDialog() {
        // given
        exceptionRule.expect(CaseidillaException.class);
        exceptionRule.expectMessage("Dialog is not found");
        Dialog foundDialog = Dialog.builder()
                .users(ImmutableSet.of(
                        User.builder()
                                .login("poupa")
                                .password("1")
                                .pin("11")
                                .build(),
                        User.builder()
                                .login("loupa")
                                .password("2")
                                .pin("22")
                                .build()
                ))
                .secret(false)
                .hidden(false)
                .id(1)
                .name(null)
                .build();
        User loupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>())
                .build();
        User poupa = User.builder()
                .pin("22")
                .password("2")
                .login("loupa")
                .dialogs(new HashSet<>(Collections.singletonList(foundDialog)))
                .build();
        doReturn(Optional.of(loupa))
                .when(userRepository).findById("loupa");
        doReturn(Optional.of(poupa))
                .when(userRepository).findById("poupa");

        // when
        dialogService.deleteDialog(DeleteDialogDto.builder()
                        .participant("poupa")
                        .build(),
                "loupa");

        // then
    }

    @Test
    public void shouldFindDialog() {
        // given
        doReturn(Optional.of(user))
                .when(userRepository).findById("poupa");

        // when
        FindDialogResponseDto actual = dialogService.findDialog("poupa", "loupa");

        //then
        assertThat(actual, is(FindDialogResponseDto.builder()
                .found(true)
                .build()));
    }
}

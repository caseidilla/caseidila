package org.codet.caseidilla.user.credentials.service.impl;

import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import org.codet.caseidilla.chat.entity.Dialog;
import org.codet.caseidilla.chat.repository.DialogRepository;
import org.codet.caseidilla.exception.CaseidillaException;
import org.codet.caseidilla.user.credentials.dto.UserLoginDto;
import org.codet.caseidilla.user.credentials.dto.UserRegistrationDto;
import org.codet.caseidilla.user.credentials.service.CredentialsService;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.codet.caseidilla.user.secret.entity.Secret;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService {

    private final UserRepository userRepository;
    private final SecretRepository secretRepository;
    private final DialogRepository dialogRepository;

    @Override
    @Transactional
    public ResponseEntity<String> register(UserRegistrationDto registrationRequest) {
        if (userRepository.findById(registrationRequest.getLogin()).isPresent()) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        Optional<Secret> secret = secretRepository.findById(registrationRequest.getSecret());
        if (!secret.isPresent()) {
            return new ResponseEntity<>("Secret code is not valid", HttpStatus.CONFLICT);
        }

        Dialog initialDialog = Dialog.builder()
                .hidden(false)
                .secret(false)
                .build();
        dialogRepository.save(initialDialog);

        User user = User.builder()
                .login(registrationRequest.getLogin())
                .password(registrationRequest.getPassword())
                .dialogs(Collections.singleton(initialDialog))
                .build();
        userRepository.save(user);
        User invitor = userRepository.findById(secret.get().getUser())
                .orElseThrow(() -> new CaseidillaException("can't find registered user for secret"));
        invitor.getDialogs().add(initialDialog);

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<String> login(UserLoginDto loginRequest) {
        Optional<User> optionalUser = userRepository.findById(loginRequest.getLogin());
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>("No such user found", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        return user.getPassword().equals(loginRequest.getPassword())
                ? ResponseEntity.ok().build()
                : new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);

    }

    @Override
    @Transactional
    public void setPin(String login, String pin) {
        User user = userRepository.findById(login)
                .orElseThrow(() -> new CaseidillaException("User not found"));
        if (user.getPin() == null) {
            user.setPin(pin);
        } else {
            throw new CaseidillaException("Pin is already set");
        }
    }
}

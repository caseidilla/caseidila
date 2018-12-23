package org.codet.caseidilla.user.credentials.service;

import org.codet.caseidilla.user.credentials.dto.CreatePinDto;
import org.codet.caseidilla.user.credentials.dto.UserLoginDto;
import org.codet.caseidilla.user.credentials.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface CredentialsService {

    ResponseEntity<String> register(UserRegistrationDto user);

    ResponseEntity<String> login(UserLoginDto user);

    @Transactional
    void setPin(String login, CreatePinDto pin);
}

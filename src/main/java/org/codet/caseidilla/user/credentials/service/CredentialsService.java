package org.codet.caseidilla.user.credentials.service;

import javax.transaction.Transactional;

import org.codet.caseidilla.user.credentials.dto.UserLoginDto;
import org.codet.caseidilla.user.credentials.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CredentialsService {

    ResponseEntity<String> register(UserRegistrationDto user);

    ResponseEntity<String> login(UserLoginDto user);

    @Transactional
    void setPin(String login, String pin);
}

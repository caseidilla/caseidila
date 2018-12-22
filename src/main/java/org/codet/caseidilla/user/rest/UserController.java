package org.codet.caseidilla.user.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codet.caseidilla.user.credentials.dto.PinDto;
import org.codet.caseidilla.user.credentials.dto.UserLoginDto;
import org.codet.caseidilla.user.credentials.dto.UserRegistrationDto;
import org.codet.caseidilla.user.credentials.service.CredentialsService;
import org.codet.caseidilla.user.secret.SecretDto;
import org.codet.caseidilla.user.secret.service.SecretService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final SecretService secretService;
    private final CredentialsService credentialsService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegistrationDto user) {
        log.trace("unauthorized user trying to register user with login=%s", user.getLogin());
        return credentialsService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto user) {
        return credentialsService.login(user);
    }

    @GetMapping("/{login}/invite")
    public SecretDto invite(@PathVariable String login) {
        return secretService.generateSecret(login);
    }

    @PostMapping("/{login}/setPin")
    public void setPin(@RequestBody PinDto pin, @PathVariable String login) {
        credentialsService.setPin(login, pin.getPin());
    }
}

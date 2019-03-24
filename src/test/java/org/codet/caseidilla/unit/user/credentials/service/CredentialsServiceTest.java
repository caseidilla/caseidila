package org.codet.caseidilla.unit.user.credentials.service;

import org.codet.caseidilla.chat.repository.DialogRepository;
import org.codet.caseidilla.user.credentials.dto.UserRegistrationDto;
import org.codet.caseidilla.user.credentials.service.impl.CredentialsServiceImpl;
import org.codet.caseidilla.user.entity.User;
import org.codet.caseidilla.user.repository.UserRepository;
import org.codet.caseidilla.user.secret.entity.Secret;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CredentialsServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecretRepository secretRepository;
    @Mock
    private DialogRepository dialogRepository;

    @InjectMocks
    private CredentialsServiceImpl credentialsService;

    @Test
    public void shouldRegister() {
        // given
        doReturn(Optional.of(Secret.builder()
                .code("111")
                .timestamp(Instant.MAX)
                .user("poupa")
                .build()))
                .when(secretRepository).findById("111");
        doReturn(Optional.of(User.builder()
                .dialogs(new HashSet<>())
                .build()))
                .when(userRepository).findById("poupa");

        // when
        ResponseEntity<String> actual = credentialsService.register(UserRegistrationDto.builder()
                .login("loupa")
                .password("22")
                .secret("111")
                .build());

        // then
        assertThat(actual, equalTo(ResponseEntity.ok().build()));

    }
}

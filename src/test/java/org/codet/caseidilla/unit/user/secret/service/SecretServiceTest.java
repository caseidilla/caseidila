package org.codet.caseidilla.unit.user.secret.service;

import org.codet.caseidilla.user.secret.SecretDto;
import org.codet.caseidilla.user.secret.entity.Secret;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.codet.caseidilla.user.secret.service.impl.SecretServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SecretServiceTest {

    @Mock
    private SecretRepository secretRepository;

    @InjectMocks
    private SecretServiceImpl secretService;

    @Test
    public void shouldGenerateSecret() {
        // given

        // when
        SecretDto actual = secretService.generateSecret("loupa");

        // then
        verify(secretRepository).save(Secret.builder()
                .user("loupa")
                .timestamp(any())
                .code(actual.getSecret())
                .build());
    }
}

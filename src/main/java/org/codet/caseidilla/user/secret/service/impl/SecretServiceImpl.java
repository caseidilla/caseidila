package org.codet.caseidilla.user.secret.service.impl;

import java.time.Instant;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.codet.caseidilla.user.secret.SecretDto;
import org.codet.caseidilla.user.secret.entity.Secret;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.codet.caseidilla.user.secret.service.SecretService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecretServiceImpl implements SecretService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SECRET_SIZE = 8;

    private final SecretRepository secretRepository;

    @Override
    @Transactional
    public SecretDto generateSecret(String login) {
        String generatedCode = generate();
        while (secretRepository.findById(generatedCode).isPresent()) {
            generatedCode = generate();
        }
        secretRepository.save(Secret.builder()
                .user(login)
                .code(generatedCode)
                .timestamp(Instant.now())
                .build());
        return SecretDto.builder()
                .secret(generatedCode)
                .build();
    }

    private String generate() {
        return IntStream.range(0, SECRET_SIZE)
                .map(i -> (int) (Math.random() * ALPHA_NUMERIC_STRING.length()))
                .map(ALPHA_NUMERIC_STRING::charAt)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

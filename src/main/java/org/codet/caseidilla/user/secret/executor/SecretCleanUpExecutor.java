package org.codet.caseidilla.user.secret.executor;

import lombok.RequiredArgsConstructor;
import org.codet.caseidilla.user.secret.repository.SecretRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class SecretCleanUpExecutor {

    private final SecretRepository secretRepository;

    @Scheduled(fixedRate = 5_000L)
    @Transactional
    void cleanUp() {
        secretRepository.deleteByTimestampBefore(Instant.now().minus(300, ChronoUnit.SECONDS));
    }
}

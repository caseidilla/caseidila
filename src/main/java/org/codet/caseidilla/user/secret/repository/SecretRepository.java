package org.codet.caseidilla.user.secret.repository;

import org.codet.caseidilla.user.secret.entity.Secret;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public interface SecretRepository extends CrudRepository<Secret, String> {
    @Modifying
    @Transactional
//    @Query("delete from SECRET s where timestamp < :timestamp")
    void deleteByTimestampBefore(Instant timestamp);
}

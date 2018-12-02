package org.codet.caseidilla.user.secret.repository;

import org.codet.caseidilla.user.secret.entity.Secret;
import org.springframework.data.repository.CrudRepository;

public interface SecretRepository extends CrudRepository<Secret, String> {
}

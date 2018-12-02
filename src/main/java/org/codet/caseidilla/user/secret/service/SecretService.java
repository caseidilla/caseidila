package org.codet.caseidilla.user.secret.service;

import org.codet.caseidilla.user.secret.SecretDto;

public interface SecretService {
    SecretDto generateSecret(String login);
}

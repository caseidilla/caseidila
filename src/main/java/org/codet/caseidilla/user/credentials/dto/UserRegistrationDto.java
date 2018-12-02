package org.codet.caseidilla.user.credentials.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserRegistrationDto {

    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private String secret;
}

package org.codet.caseidilla.user.secret;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecretDto {
    private final String secret;
}

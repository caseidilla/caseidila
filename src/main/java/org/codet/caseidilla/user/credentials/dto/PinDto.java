package org.codet.caseidilla.user.credentials.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PinDto {
    private String pin;
    private String oldPin;
}

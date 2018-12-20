package org.codet.caseidilla.chat.dto;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SendMessageDto {
    private String from;
    private String body;
    private Instant timestamp;
}

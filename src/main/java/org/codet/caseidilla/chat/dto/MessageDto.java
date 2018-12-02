package org.codet.caseidilla.chat.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {
    private final MessageType type;
    private final String body;
    private final Instant timestamp;

    public enum MessageType {
        @JsonProperty("sent")
        SENT,
        @JsonProperty("received")
        RECEIVED
    }
}

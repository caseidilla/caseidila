package org.codet.caseidilla.chat.entity.id;

import java.io.Serializable;
import java.time.Instant;

public class MessageId implements Serializable {
    String sender;
    Instant timestamp;
}

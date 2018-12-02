package org.codet.caseidilla.chat.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;

import org.codet.caseidilla.chat.entity.id.MessageId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@IdClass(MessageId.class)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Message {

    @Id
    @Column(nullable = false)
    private String sender;
    @Id
    @Column(nullable = false)
    private String receiver;
    @Id
    @Column(nullable = false)
    private Instant timestamp;
    @Lob
    @Column(nullable = false)
    private String body;
}

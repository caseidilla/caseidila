package org.codet.caseidilla.chat.repository;

import java.util.List;

import org.codet.caseidilla.chat.entity.Message;
import org.codet.caseidilla.chat.entity.id.MessageId;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, MessageId> {
    List<Message> findAllBySenderAndReceiver(String sender, String receiver);
}

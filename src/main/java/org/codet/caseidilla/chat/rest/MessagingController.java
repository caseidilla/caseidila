package org.codet.caseidilla.chat.rest;

import lombok.RequiredArgsConstructor;
import org.codet.caseidilla.chat.dto.IncomingMessageDto;
import org.codet.caseidilla.chat.dto.SendMessageDto;
import org.codet.caseidilla.chat.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessagingController {

    private final MessageService messageService;

    @MessageMapping("/chat.send")
    @SendToUser("/message")
    public SendMessageDto sendMessage(@Payload IncomingMessageDto message) {
        return messageService.sendMessage(message);
    }
}

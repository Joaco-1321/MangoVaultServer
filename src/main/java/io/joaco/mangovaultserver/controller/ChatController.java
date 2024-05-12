package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handle(Message message) {
        System.out.println(message.toString());
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/chat", message);
    }
}

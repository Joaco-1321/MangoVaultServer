package io.joaco.mangovaultserver.controller;

import io.joaco.mangovaultserver.domain.model.Message;
import io.joaco.mangovaultserver.facade.FriendFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final FriendFacade friendFacade;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void handle(Principal principal, Message message) {
        if (principal != null && principal.getName()
                                          .equals(message.getSender())) {
            friendFacade.isFriendOrThrow(message.getSender(), message.getRecipient());
            messagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/chat", message);
            System.out.println(message);
        }
    }
}

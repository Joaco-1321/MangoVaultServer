package io.joaco.mangovaultserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = extractUsernameFromQueryString(session.getUri().getRawQuery());
        sessions.put(username, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(message.getPayload(), Message.class);

        // Get recipient's WebSocket session
        WebSocketSession recipientSession = sessions.get(msg.getTo());
        if (recipientSession != null) {
            // Send the message to the recipient's WebSocket session
            recipientSession.sendMessage(new TextMessage(msg.getMessage()));
        }
    }

    private String extractUsernameFromQueryString(String queryString) {
        String[] parts = queryString.split("=");
        return parts[1];
    }
}

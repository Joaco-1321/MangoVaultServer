package io.joaco.mangovaultserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.joaco.mangovaultserver.model.Message;
import io.joaco.mangovaultserver.model.User;
import io.joaco.mangovaultserver.services.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // maps json to objects
        ObjectMapper mapper = new ObjectMapper();
        // creates a node tree with the values of the json
        JsonNode jsonNode = mapper.readTree(message.getPayload());

        // if the json contains username and password, authenticates the user
        // else, closes the connection
        if (jsonNode.has("username") && jsonNode.has("password")) {
            Optional<User> authenticatedUser = AuthService.authenticate(
                    jsonNode.get("username").asText(),
                    jsonNode.get("password").asText()
            );

            ObjectNode response = mapper.createObjectNode();

            if (authenticatedUser.isPresent()) {
                sessions.put(authenticatedUser.get().getUsername(), session);
                response.put("success", true);
                session.sendMessage(new TextMessage(response.toString()));
            } else {
                response.put("success", false);
                session.sendMessage(new TextMessage(response.toString()));
                session.close(CloseStatus.PROTOCOL_ERROR.withReason("authentication failed"));
            }
        } else {
            // map json to Message object
            Message msg = mapper.readValue(message.getPayload(), Message.class);
            // get the session of recipient
            WebSocketSession recipientSession = sessions.get(msg.getTo());

            if (recipientSession != null) {
                recipientSession.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }
}

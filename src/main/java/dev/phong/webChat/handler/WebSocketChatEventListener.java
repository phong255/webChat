package dev.phong.webChat.handler;

import dev.phong.webChat.common.ChatMessage;
import dev.phong.webChat.service.ChatBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketChatEventListener {
    private final Logger logger = LoggerFactory.getLogger(WebSocketChatEventListener.class);
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private ChatBotService chatBotService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new joining {}", event.getUser());
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {
        logger.info("Received a new subscriber");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if(username != null) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);
            logger.info("Room - {} : User {} has left !",roomId,username);
        }
    }
}

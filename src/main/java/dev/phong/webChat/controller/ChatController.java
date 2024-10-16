package dev.phong.webChat.controller;

import dev.phong.webChat.common.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("eSoft/chat.sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable(value = "roomId") String roomId
    ){
        logger.info("Room {} - {}: {}", roomId, chatMessage.getSender(), chatMessage.getContent());
        return chatMessage;
    }

    @MessageMapping("eSoft/chat.newUser/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage newUser(
            @Payload ChatMessage chatMessage,
            @DestinationVariable(value = "roomId") String roomId,
            SimpMessageHeaderAccessor headerAccessor
    ){
        logger.info("{} joining room {}", chatMessage.getSender(), roomId);
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("eSoft/typing/{roomId}")
    @SendTo("/topic/typing/{roomId}")
    public String typingEvent(@Payload ChatMessage chatMessage) {
        return "User " + (chatMessage == null ? "" : chatMessage.getSender()) + " is typing...";
    }
}

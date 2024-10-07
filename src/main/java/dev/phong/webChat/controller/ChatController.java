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
        logger.info(String.format("Room %s - %s: %s",roomId,chatMessage.getSender(),chatMessage.getContent()));
        return chatMessage;
    }

    @MessageMapping("eSoft/chat.newUser/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage newUser(
            @Payload ChatMessage chatMessage,
            @DestinationVariable(value = "roomId") String roomId,
            SimpMessageHeaderAccessor headerAccessor
    ){
        logger.info(chatMessage.getSender() + " joining room " + roomId);
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}

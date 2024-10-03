package dev.phong.webChat.controller;

import dev.phong.webChat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("eSoft/{roomId}/chat.sendMessage")
    @SendTo("/topic/eSoft/{roomId}")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ){
        return chatMessage;
    }

    @MessageMapping("eSoft/{roomId}/chat.newUser")
    @SendTo("/topic/eSoft/{roomId}")
    public ChatMessage newUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("office/{roomId}/chat.sendMessage")
    @SendTo("/topic/office/{roomId}")
    public ChatMessage sendMessageOffice(
            @Payload ChatMessage chatMessage
    ){
        return chatMessage;
    }

    @MessageMapping("office/{roomId}/chat.newUser")
    @SendTo("/topic/office/{roomId}")
    public ChatMessage newUserOffice(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}

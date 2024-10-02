package dev.phong.webChat.controller;

import dev.phong.webChat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/eSoft")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ){
        return chatMessage;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/eSoft")
    public ChatMessage newUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}

package dev.phong.webChat.controller;

import dev.phong.webChat.common.ChatMessage;
import dev.phong.webChat.config.loader.CommonServiceProperties;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.service.ChatBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

@Controller
public class ChatController {
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatBotService chatBotService;
    @Autowired
    private CommonServiceProperties properties;

    @MessageMapping("eSoft/chat.sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable(value = "roomId") String roomId
    ){
        if(!chatMessage.getSender().equals(properties.getNameOfBot())){
            CompletableFuture.runAsync(() -> {
                try {
                    chatBotService.sendMessage(roomId, chatMessage);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return chatMessage;
    }

    @MessageMapping("eSoft/chat.newUser/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage newUser(
            @Payload ChatMessage chatMessage,
            @DestinationVariable(value = "roomId") String roomId,
            SimpMessageHeaderAccessor headerAccessor
    ){
        logger.info("Room - {} : User {} has joined.", roomId, chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomId",roomId);
        return chatMessage;
    }

    @MessageMapping("eSoft/typing/{roomId}")
    @SendTo("/topic/{roomId}")
    public String typingEvent(@Payload ChatMessage chatMessage) {
        if(chatMessage == null){
            chatMessage = new ChatMessage();
        }
        chatMessage.setType(ChatMessage.MessageType.TYPING);
        return "User " + chatMessage.getSender() + " is typing...";
    }
}

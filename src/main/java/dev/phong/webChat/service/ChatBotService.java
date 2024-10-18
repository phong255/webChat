package dev.phong.webChat.service;

import dev.phong.webChat.common.ChatMessage;
import org.springframework.stereotype.Service;

@Service
public interface ChatBotService {
    void sendMessage(String roomId, ChatMessage chatMessage) throws InterruptedException;
}

package dev.phong.webChat.service;

import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.entity.Questions;
import org.springframework.stereotype.Service;

@Service
public interface AIChatService {
    Questions reply(String payLoad);
}

package dev.phong.webChat.service.Impl;

import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.entity.Questions;
import dev.phong.webChat.repository.QuestionsRepository;
import dev.phong.webChat.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIChatServiceImpl implements AIChatService {
    @Autowired
    QuestionsRepository questionsRepository;
    @Override
    public Questions reply(String payLoad) {
        List<Questions> questions = questionsRepository.findAll();
        List<String> letters = Arrays.stream(payLoad.toLowerCase().split("\\s+")).toList();
        Map<Integer, Questions> rate = new HashMap<>();
        for(Questions question : questions){
            List<String> cutting = new ArrayList<>(Arrays.stream(question.getContent().toLowerCase().split("\\s+")).toList());
            cutting.retainAll(letters);
            rate.putIfAbsent(cutting.size(),question);
        }
        Integer maxRate = Collections.max(rate.keySet());
        return maxRate.equals(0) ? null : rate.get(maxRate);
    }
}

package dev.phong.webChat.service.Impl;

import dev.phong.webChat.common.ChatMessage;
import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.config.loader.CommonServiceProperties;
import dev.phong.webChat.entity.Answers;
import dev.phong.webChat.entity.Questions;
import dev.phong.webChat.repository.AnswersRepository;
import dev.phong.webChat.repository.QuestionsRepository;
import dev.phong.webChat.service.AIChatService;
import dev.phong.webChat.service.ChatBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatBotMessageImpl implements ChatBotService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CommonServiceProperties commonServiceProperties;
    @Autowired
    private AIChatService aiChatService;
    @Autowired
    private QuestionsRepository questionsRepository;
    @Autowired
    private AnswersRepository answersRepository;

    @Override
    public void sendMessage(String roomId, ChatMessage chatMessage) throws InterruptedException {
        Thread.sleep(1000);
        Questions questions = aiChatService.reply(chatMessage.getContent());
        if(questions != null){
            Questions questions_new;
            if(questions.getContent().compareToIgnoreCase(chatMessage.getContent()) == 0){
                questions_new = questions;
            }
            else {
                questions_new = questionsRepository.save(
                        new Questions(
                                chatMessage.getContent(),
                                null,
                                QuestionsCategory.INFO
                        )
                );
            }
            if(!questions.getAnswers().isEmpty()){
                for(Answers answers : questions.getAnswers()){
                    answersRepository.save(
                            new Answers(answers.getContent(),answers.getLink(), answers.getImage(),questions_new)
                    );
                    messagingTemplate.convertAndSend(
                            "/topic/" + roomId,
                            new ChatMessage(ChatMessage.MessageType.CHAT,answers.getContent(),commonServiceProperties.getNameOfBot())
                    );
                }
            }
        }
        else{
            messagingTemplate.convertAndSend(
                    "/topic/" + roomId,
                    new ChatMessage(ChatMessage.MessageType.CHAT,"Hi, I'm " + commonServiceProperties.getNameOfBot(),commonServiceProperties.getNameOfBot())
            );
        }
    }
}

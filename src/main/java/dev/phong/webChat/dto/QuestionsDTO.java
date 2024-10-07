package dev.phong.webChat.dto;

import dev.phong.webChat.entity.Answers;
import dev.phong.webChat.entity.Questions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDTO {
    private Long id;
    private String content;
    private String image;
    private List<AnswersDTO> answersDTOS;

    public QuestionsDTO(Questions questions){
        this.id = questions.getId();
        this.content = questions.getContent();
        this.image = questions.getImage();
        this.answersDTOS = questions.getAnswers()
                .stream()
                .map(AnswersDTO::new)
                .toList();
    }

    public Questions toCreate(){
        Questions questions = new Questions();
        questions.setContent(this.content);
        questions.setImage(this.image);
        return questions;
    }

    public Questions toUpdate(Questions questions){
        questions.setContent(this.content);
        questions.setImage(this.image);
        return questions;
    }
}

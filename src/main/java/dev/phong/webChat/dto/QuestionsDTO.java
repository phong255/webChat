package dev.phong.webChat.dto;

import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.entity.Answers;
import dev.phong.webChat.entity.Questions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDTO {
    private Long id;
    @NotNull(message = "Content not null")
    private String content;
    private String image;
    @NotNull(message = "Category not null")
    private QuestionsCategory category;
    private List<AnswersDTO> answersDTOS;

    public QuestionsDTO(Questions questions){
        this.id = questions.getId();
        this.content = questions.getContent();
        this.image = questions.getImage();
        this.category = questions.getCategory();
        this.answersDTOS = questions.getAnswers()
                .stream()
                .map(AnswersDTO::new)
                .toList();
    }

    public Questions toCreate(){
        Questions questions = new Questions();
        questions.setContent(this.content);
        questions.setImage(this.image);
        questions.setCategory(this.category);
        return questions;
    }

    public Questions toUpdate(Questions questions){
        questions.setContent(this.content);
        questions.setImage(this.image);
        questions.setCategory(this.category);
        return questions;
    }
}

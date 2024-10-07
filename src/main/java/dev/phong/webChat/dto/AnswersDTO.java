package dev.phong.webChat.dto;

import dev.phong.webChat.entity.Answers;
import dev.phong.webChat.entity.Questions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswersDTO {
    private Long id;

    private String content;

    private String link;

    private String image;

    public AnswersDTO(Answers answers){
        this.id = answers.getId();
        this.content = answers.getContent();
        this.link = answers.getLink();
        this.image = answers.getImage();
    }

    public Answers toCreate(Questions questions){
        Answers answers = new Answers();
        answers.setContent(this.content);
        answers.setLink(this.link);
        answers.setImage(this.image);
        answers.setQuestions(questions);
        return answers;
    }

    public Answers toUpdate(Answers answers, Questions questions){
        answers.setContent(this.content);
        answers.setLink(this.link);
        answers.setImage(this.image);
        answers.setQuestions(questions);
        return answers;
    }
}

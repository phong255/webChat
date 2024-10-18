package dev.phong.webChat.entity;

import dev.phong.webChat.common.QuestionsCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String image;
    private QuestionsCategory category;
    @OneToMany(mappedBy = "questions", fetch = FetchType.EAGER)
    private List<Answers> answers;

    public Questions(String content, String image, QuestionsCategory questionsCategory) {
        this.content = content;
        this.image = image;
        this.category = questionsCategory;
    }
}

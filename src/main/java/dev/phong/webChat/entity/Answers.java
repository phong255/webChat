package dev.phong.webChat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answers")
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String link;

    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questionsId", referencedColumnName = "id")
    private Questions questions;
}

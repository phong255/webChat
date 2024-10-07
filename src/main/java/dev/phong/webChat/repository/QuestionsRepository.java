package dev.phong.webChat.repository;

import dev.phong.webChat.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionsRepository extends JpaRepository<Questions, Long>, JpaSpecificationExecutor<Questions> {
}

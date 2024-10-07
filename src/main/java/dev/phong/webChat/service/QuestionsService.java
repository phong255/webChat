package dev.phong.webChat.service;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.dto.QuestionsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionsService {
    PageDto<QuestionsDTO> getList(QuestionsCategory category, String id, String searchAll, Integer pageIndex, Integer pageSize);
    List<QuestionsDTO> getAll(QuestionsCategory category, String id, String searchAll);
    QuestionsDTO getDetail(Long id);
    QuestionsDTO create(QuestionsDTO questionsDTO);
    QuestionsDTO update(Long id, QuestionsDTO questionsDTO);
    void delete(Long id);
}

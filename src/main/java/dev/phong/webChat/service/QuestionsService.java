package dev.phong.webChat.service;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.dto.QuestionsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionsService {
    PageDto<QuestionsDTO> getList(Long id, String searchAll, Integer pageIndex, Integer pageSize);
    List<QuestionsDTO> getAll(Long id, String searchAll);
    QuestionsDTO getDetail(Long id);
    QuestionsDTO create(QuestionsDTO questionsDTO);
    QuestionsDTO update(Long id, QuestionsDTO questionsDTO);
    void delete(Long id);
}

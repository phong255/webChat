package dev.phong.webChat.service.Impl;

import dev.phong.webChat.common.MessageConstant;
import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.dto.AnswersDTO;
import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.entity.Questions;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.repository.AnswersRepository;
import dev.phong.webChat.repository.QuestionsRepository;
import dev.phong.webChat.service.QuestionsService;
import dev.phong.webChat.util.QueryUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionsServiceImpl implements QuestionsService {
    private QuestionsRepository questionsRepository;
    private AnswersRepository answersRepository;
    @Override
    public PageDto<QuestionsDTO> getList(Long id, String search, Integer pageIndex, Integer pageSize) {
        Specification<Questions> specification = (root, query, cb) -> cb.and(
                QueryUtils.buildEqFilter(root,cb,"id",id),
                QueryUtils.buildLikeFilter(root,cb,search,"content","image"),
                QueryUtils.joinAndLike(root,cb,"content",search,"answers"),
                QueryUtils.joinAndLike(root,cb,"link",search,"answers"),
                QueryUtils.joinAndLike(root,cb,"image",search,"answers")
        );
        Page<Questions> questionsDTOS = questionsRepository.findAll(specification, PageRequest.of(pageIndex - 1, pageSize, Sort.by("id").descending()));
        return PageDto.of(
                questionsDTOS,
                questionsDTOS.stream()
                        .map(QuestionsDTO::new)
                        .toList()
        );
    }

    @Override
    public List<QuestionsDTO> getAll(Long id, String search) {
        Specification<Questions> specification = (root, query, cb) -> cb.and(
                QueryUtils.buildEqFilter(root,cb,"id",id),
                QueryUtils.buildLikeFilter(root,cb,search,"content","image"),
                QueryUtils.joinAndLike(root,cb,"content",search,"answers"),
                QueryUtils.joinAndLike(root,cb,"link",search,"answers"),
                QueryUtils.joinAndLike(root,cb,"image",search,"answers")
        );
        List<Questions> questionsDTOS = questionsRepository.findAll(specification, Sort.by("id").descending());
        return questionsDTOS.stream()
                .map(QuestionsDTO::new)
                .toList();
    }

    @Override
    public QuestionsDTO getDetail(Long id) {
        Questions questions = questionsRepository.findById(id).orElseThrow(()->new CustomException(MessageConstant.QuestionNotFound + "id " + id));
        return new QuestionsDTO(questions);
    }

    @Override
    public QuestionsDTO create(QuestionsDTO questionsDTO) {
//        Questions questions = questionsDTO.toCreate();
//        questions = questionsRepository.save(questions);
//        for(AnswersDTO answersDTO)
        return null;
    }

    @Override
    public QuestionsDTO update(Long id, QuestionsDTO questionsDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

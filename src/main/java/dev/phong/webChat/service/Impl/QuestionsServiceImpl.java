package dev.phong.webChat.service.Impl;

import dev.phong.webChat.common.MessageConstant;
import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.dto.AnswersDTO;
import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.entity.Answers;
import dev.phong.webChat.entity.Questions;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.repository.AnswersRepository;
import dev.phong.webChat.repository.QuestionsRepository;
import dev.phong.webChat.service.QuestionsService;
import dev.phong.webChat.util.QueryUtils;
import jakarta.transaction.Transactional;
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
    public PageDto<QuestionsDTO> getList(QuestionsCategory category, String id, String search, Integer pageIndex, Integer pageSize) {
        Specification<Questions> specification = (root, query, cb) -> cb.and(
                id == null ? cb.and() : cb.like(cb.upper(cb.toString(root.get("id"))),id),
                QueryUtils.buildEqFilter(root,cb,"category",category),
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
    public List<QuestionsDTO> getAll(QuestionsCategory category, String id, String search) {
        Specification<Questions> specification = (root, query, cb) -> cb.and(
                id == null ? cb.and() : cb.like(cb.upper(cb.toString(root.get("id"))),id),
                QueryUtils.buildEqFilter(root,cb,"category",category),
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

    @Transactional
    @Override
    public QuestionsDTO create(QuestionsDTO questionsDTO) {
        Questions questions = questionsDTO.toCreate();
        if(questionsRepository.existsByContent(questions.getContent())){
            throw new CustomException(MessageConstant.QuestionDuplicate);
        }
        questions = questionsRepository.save(questions);
        questionsDTO.setId(questions.getId());
        for(AnswersDTO answersDTO : questionsDTO.getAnswersDTOS()){
            Answers answers = answersDTO.toCreate(questions);
            answers = answersRepository.save(answers);
            answersDTO.setId(answers.getId());
        }
        return questionsDTO;
    }

    @Transactional
    @Override
    public QuestionsDTO update(Long id, QuestionsDTO questionsDTO) {
        Questions questions = questionsRepository.findById(id).orElseThrow(()->new CustomException(MessageConstant.QuestionNotFound + "id:" + id));
        questions = questionsDTO.toUpdate(questions);
        if(questionsRepository.existsByContentAndIdNot(questions.getContent(),id)){
            throw new CustomException(MessageConstant.QuestionDuplicate);
        }
        for(AnswersDTO answersDTO : questionsDTO.getAnswersDTOS()){
            Answers answers = answersRepository.findById(id).orElseThrow(()->new CustomException(MessageConstant.AnswerNotFound + "id:" + id));
            answers = answersDTO.toUpdate(answers, questions);
            answersRepository.save(answers);
        }
        return new QuestionsDTO(questions);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Questions questions = questionsRepository.findById(id).orElseThrow(()->new CustomException(MessageConstant.QuestionNotFound + "id:" + id));
        answersRepository.deleteAll(questions.getAnswers());
        questionsRepository.delete(questions);
    }
}

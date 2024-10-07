package dev.phong.webChat.controller;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.service.QuestionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {
    private QuestionsService questionsService;

    @GetMapping()
    public PageDto<QuestionsDTO> getList(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "category", required = false) QuestionsCategory category,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "pageSize") Integer pageSize
    ){
        return questionsService.getList(category,id,search,pageIndex,pageSize);
    }

    @GetMapping("/all")
    public List<QuestionsDTO> getAll(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "category", required = false) QuestionsCategory category,
            @RequestParam(value = "search", required = false) String search
    ){
        return questionsService.getAll(category,id,search);
    }

    @GetMapping("/{id}")
    public QuestionsDTO detail(
            @PathVariable(value = "id") Long id
    ){
        return questionsService.getDetail(id);
    }

    @PostMapping
    public QuestionsDTO create(
            @RequestBody @NotNull @Valid QuestionsDTO questionsDTO
    ){
        return questionsService.create(questionsDTO);
    }

    @PutMapping("/{id}")
    public QuestionsDTO update(
            @PathVariable(value = "id") Long id,
            @RequestBody @NotNull @Valid QuestionsDTO questionsDTO
    ){
        return questionsService.update(id, questionsDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable(value = "id") Long id
    ){
        questionsService.delete(id);
    }
}

package dev.phong.webChat.controller;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.common.QuestionsCategory;
import dev.phong.webChat.dto.QuestionsDTO;
import dev.phong.webChat.service.QuestionsService;
import dev.phong.webChat.util.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Question", description = "Question api")
@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {
    private QuestionsService questionsService;

    @Operation(summary = "Get list question pagination")
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

    @Operation(summary = "Get all question")
    @GetMapping("/all")
    public List<QuestionsDTO> getAll(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "category", required = false) QuestionsCategory category,
            @RequestParam(value = "search", required = false) String search
    ){
        return questionsService.getAll(category,id,search);
    }

    @Operation(summary = "Get detail question")
    @GetMapping("/{id}")
    public QuestionsDTO detail(
            @PathVariable(value = "id") Long id
    ){
        return questionsService.getDetail(id);
    }

    @Operation(summary = "Add new question")
    @PostMapping
    public QuestionsDTO create(
            @Valid @RequestBody @NotNull QuestionsDTO questionsDTO,
            BindingResult bindingResult
    ){
        ValidUtils.throwErrors(bindingResult);
        return questionsService.create(questionsDTO);
    }

    @Operation(summary = "Update")
    @PutMapping("/{id}")
    public QuestionsDTO update(
            @PathVariable(value = "id") Long id,
            @RequestBody @NotNull @Valid QuestionsDTO questionsDTO,
            BindingResult bindingResult
    ){
        ValidUtils.throwErrors(bindingResult);
        return questionsService.update(id, questionsDTO);
    }

    @Operation(summary = "Delete")
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable(value = "id") Long id
    ){
        questionsService.delete(id);
    }
}

package dev.phong.webChat.controller;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.dto.Request.LoginRequest;
import dev.phong.webChat.dto.Response.UserLogin;
import dev.phong.webChat.dto.UserDto;
import dev.phong.webChat.service.AuthenticatedService;
import dev.phong.webChat.service.UserService;
import dev.phong.webChat.util.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User API")
@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @Operation(summary = "get list user pagination")
    @GetMapping
    public PageDto<UserDto> getList(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "isLogin", required = false) Boolean isLogin,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "pageSize") Integer pageSize
    ){
        return userService.getList(search,isLogin,pageIndex,pageSize);
    }

    @Operation(summary = "get all")
    @GetMapping("/all")
    public List<UserDto> getAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "isLogin", required = false) Boolean isLogin
    ){
        return userService.getAll(search,isLogin);
    }

    @Operation(summary = "detail")
    @GetMapping("/detail/{id}")
    public UserDto detail(
            @PathVariable(value = "id") Integer id
    ){
        return userService.getById(id);
    }

    @Operation(summary = "Create")
    @PostMapping
    public UserDto add(
            @RequestBody @Valid @NotNull UserDto userDto,
            BindingResult bindingResult
    ){
        ValidUtils.throwErrors(bindingResult);
        return userService.add(userDto);
    }

    @Operation(summary = "Update")
    @PutMapping("{id}")
    public UserDto update(
            @PathVariable(value = "id") Integer id,
            @RequestBody @Valid @NotNull UserDto userDto,
            BindingResult bindingResult
    ){
        ValidUtils.throwErrors(bindingResult);
        return userService.update(id, userDto);
    }

    @Operation(summary = "Delete")
    @DeleteMapping("{id}")
    public void delete(
            @PathVariable(value = "id") Integer id
    ){
        userService.delete(id);
    }
}

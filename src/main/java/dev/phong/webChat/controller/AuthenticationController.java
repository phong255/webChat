package dev.phong.webChat.controller;

import dev.phong.webChat.dto.Request.LoginRequest;
import dev.phong.webChat.dto.Response.UserLogin;
import dev.phong.webChat.service.AuthenticatedService;
import dev.phong.webChat.util.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication API")
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticatedService authenticatedService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public UserLogin login(
            @RequestBody @Valid @NotNull LoginRequest loginRequest,
            BindingResult bindingResult
    ){
        ValidUtils.throwErrors(bindingResult);
        return authenticatedService.login(loginRequest);
    }

    @Operation(summary = "Logout")
    @PutMapping("/logout/{id}")
    public void logout(@PathVariable(value = "id") Integer id){
        authenticatedService.logout(id);
    }

}

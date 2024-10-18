package dev.phong.webChat.service;

import dev.phong.webChat.dto.Request.LoginRequest;
import dev.phong.webChat.dto.Response.UserLogin;
import dev.phong.webChat.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticatedService {
    public UserLogin login(LoginRequest loginRequest);
    public void logout(Integer id);
    public UserDto signUp(UserDto user);
}

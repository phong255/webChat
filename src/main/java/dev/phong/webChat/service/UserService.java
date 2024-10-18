package dev.phong.webChat.service;

import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.dto.UserDto;
import dev.phong.webChat.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public UserDto getById(Integer userID);
    public UserDetailsService userDetailsService();
    public UserDto update(Integer id, UserDto user);
    public UserDto add(UserDto user);
    public PageDto<UserDto> getList(String search, Boolean isLogin, Integer pageIndex, Integer pageSize);
    public List<UserDto> getAll(String search, Boolean isLogin);
    public void delete(Integer id);
}
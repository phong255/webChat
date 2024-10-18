package dev.phong.webChat.service.Impl;

import dev.phong.webChat.common.MessageConstant;
import dev.phong.webChat.common.ServiceError;
import dev.phong.webChat.config.security.JwtService;
import dev.phong.webChat.dto.Request.LoginRequest;
import dev.phong.webChat.dto.Response.UserLogin;
import dev.phong.webChat.dto.UserDto;
import dev.phong.webChat.entity.User;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.handler.exceptionHandler.exeption.UnauthorizedException;
import dev.phong.webChat.repository.UserRepo;
import dev.phong.webChat.service.AuthenticatedService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticatedServiceImpl implements AuthenticatedService {
    private UserRepo userRepo;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    @Override
    public UserLogin login(LoginRequest loginRequest) {
        User user_save = userRepo.getUserByUsername(loginRequest.getUsername());
        if(user_save == null || !passwordEncoder.matches(loginRequest.getPassword(),user_save.getPassword())){
            throw new UnauthorizedException(ServiceError.ServiceErrors.USERNAME_OR_PASSWORD_NOT_CORRECT);
        }
        user_save.setIsLogin(true);
        user_save = userRepo.save(user_save);
        UserDto userDto = new UserDto(user_save);
        UserLogin userLogin = new UserLogin();
        userLogin.setUserDto(userDto);
        userLogin.setToken(jwtService.generateToken(userDto));
        return userLogin;
    }

    @Override
    public void logout(Integer id) {
        User user = userRepo.findById(id).orElseThrow(()->new CustomException(MessageConstant.USER_NOT_FOUND));
        user.setIsLogin(false);
        userRepo.save(user);
    }

    @Transactional
    @Override
    public UserDto signUp(UserDto user_form) {
        User userDB = userRepo.getUserByUsername(user_form.getUsername());
        if(userDB != null){
            throw new CustomException(MessageConstant.USER_IS_EXIST);
        }
        //------ save user info ---------
        user_form.setPassword(passwordEncoder.encode(user_form.getPassword()));
        User user = userRepo.save(user_form.toEntity());
        return new UserDto(user);
    }
}

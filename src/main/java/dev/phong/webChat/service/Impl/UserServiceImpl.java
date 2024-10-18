package dev.phong.webChat.service.Impl;

import dev.phong.webChat.common.MessageConstant;
import dev.phong.webChat.common.PageDto;
import dev.phong.webChat.dto.UserDto;
import dev.phong.webChat.entity.User;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.repository.UserRepo;
import dev.phong.webChat.service.UserService;
import dev.phong.webChat.util.QueryUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;

    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userRepo.getUserByUsername(username);
            if(user == null){
                throw new CustomException(MessageConstant.USER_NOT_FOUND);
            }
            return new UserDto(user);
        };
    }

    @Override
    public UserDto update(Integer id, UserDto userDto) {
        User user = userRepo.findById(id).orElseThrow(()->new CustomException(MessageConstant.USER_NOT_FOUND));
        user.setAvatar(userDto.getAvatar());
        user.setEmail(userDto.getEmail());
        if(new BCryptPasswordEncoder().encode(userDto.getPassword()).compareToIgnoreCase(user.getPassword()) != 0){
            user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        }
        if(userDto.getUsername().compareToIgnoreCase(user.getUsername()) != 0){
            if(userRepo.existsByUsernameAndIdNot(userDto.getUsername(), id)){
                throw new CustomException(MessageConstant.USER_IS_EXIST);
            }
            user.setUsername(userDto.getUsername());
        }
        user.setPhone(userDto.getPhone());
        user = userRepo.save(user);
        return new UserDto(user);
    }

    @Override
    public UserDto add(UserDto user) {
        if(userRepo.existsByUsername(user.getUsername())){
            throw new CustomException(MessageConstant.USER_IS_EXIST);
        }
        User userNew = user.toEntity();
        userNew.setPassword(new BCryptPasswordEncoder().encode(userNew.getPassword()));
        userNew.setIsLogin(false);
        userNew = userRepo.save(userNew);
        return new UserDto(userNew);
    }

    @Override
    public PageDto<UserDto> getList(String search, Boolean isLogin, Integer pageIndex, Integer pageSize) {
        Specification<User> specification = (root, query, cb) -> cb.and(
                QueryUtils.buildLikeFilter(root,cb,search,"username","password","avatar","phone","email","fullName"),
                QueryUtils.buildEqFilter(root,cb,"isLogin",isLogin)
        );
        Page<User> userPage = userRepo.findAll(specification, PageRequest.of(pageIndex - 1, pageSize, Sort.by("id").descending()));
        return PageDto.of(
                userPage,
                userPage.getContent()
                        .stream()
                        .map(UserDto::new)
                        .toList()
        );
    }

    @Override
    public List<UserDto> getAll(String search, Boolean isLogin) {
        Specification<User> specification = (root, query, cb) -> cb.and(
                QueryUtils.buildLikeFilter(root,cb,search,"username","password","avatar","phone","email","fullName"),
                QueryUtils.buildEqFilter(root,cb,"isLogin",isLogin)
        );
        return userRepo.findAll(specification)
                .stream()
                .map(UserDto::new)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    public UserDto getById(Integer userID) {
        return new UserDto(userRepo.findById(userID).orElseThrow(() -> new CustomException(MessageConstant.USER_NOT_FOUND)));
    }

}
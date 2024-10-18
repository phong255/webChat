package dev.phong.webChat.dto.Response;

import dev.phong.webChat.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    private UserDto userDto;
    private String token;
}

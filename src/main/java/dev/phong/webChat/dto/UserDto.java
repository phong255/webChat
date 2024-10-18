package dev.phong.webChat.dto;

import dev.phong.webChat.entity.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements UserDetails {
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String username;
    private String password;
    private String avatar;
    private String fullName;
    @Pattern(regexp = "^0[1-9][0-9]{8}$")
    private String phone;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;
    private Boolean isLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roleNames = new ArrayList<>();
        roleNames.add(new SimpleGrantedAuthority("ADMIN"));
        return roleNames;
    }

    public UserDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.isLogin = user.getIsLogin();
    }

    public User toEntity(){
        return new User(
                this.id,
                this.username,
                this.password,
                this.avatar,
                this.phone,
                this.email,
                this.fullName,
                this.isLogin
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isLogin;
    }
}
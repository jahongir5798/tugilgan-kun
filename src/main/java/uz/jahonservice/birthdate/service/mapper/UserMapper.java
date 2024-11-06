package uz.jahonservice.birthdate.service.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.jahonservice.birthdate.dto.SignInDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.entity.User;
import uz.jahonservice.birthdate.service.impl.UserServiceImpl;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    private final UserServiceImpl userServiceImpl;

    public UserMapper(PasswordEncoder passwordEncoder, @Lazy UserServiceImpl userServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
    }

    public User toUserEntity(SignUpDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .birthDate(dto.getBirthDate())
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .untilBirthDate(this.userServiceImpl.leftDays(user.getBirthDate()))
                .build();
    }

     public User toUserEntityForBuildToken(SignInDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }
}

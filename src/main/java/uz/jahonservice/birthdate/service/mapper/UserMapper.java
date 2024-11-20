package uz.jahonservice.birthdate.service.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.jahonservice.birthdate.dto.SignInDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.dto.UserUpdateDto;
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
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .birthDate(user.getBirthDate())
                .untilBirthDate(this.userServiceImpl.leftDays(user.getBirthDate()))
                .build();
    }

     public User toUserEntityForBuildToken(SignInDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .build();
    }


    public User updatedUser(User oldUser, UserUpdateDto userDto) {
        if (userDto.getFirstName() != null) oldUser.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) oldUser.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) oldUser.setEmail(userDto.getEmail());
        if (userDto.getBirthDate() != null) oldUser.setBirthDate(userDto.getBirthDate());
        return oldUser;
    }
}

package uz.jahonservice.birthdate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignInDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.entity.User;
import uz.jahonservice.birthdate.exceptions.MyException;
import uz.jahonservice.birthdate.repository.UserRepository;
import uz.jahonservice.birthdate.service.AuthService;
import uz.jahonservice.birthdate.service.jwtService.JwtService;
import uz.jahonservice.birthdate.service.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<UserDto> registration(SignUpDto dto){
        if (this.userRepository.existsByEmail(dto.getEmail())) {
            throw new MyException("This email already exist");
        }

        if (!dto.getPassword().equals(dto.getPrePassword())) {
            throw new MyException("Passwords do not match");
        }

        User user = this.userMapper.toUserEntity(dto);
        user = this.userRepository.save(user);
        UserDto userDto = this.userMapper.toDto(user);

        return ApiResponse.<UserDto>builder()
                .code(0)
                .message("User registration successful")
                .success(true)
                .data(userDto)
                .build();
    }

    @Override
    public ApiResponse<String> login(SignInDto signInDto){

        User user = this.userRepository.findByEmail(signInDto.getEmail()).orElseThrow(() -> new MyException("This user does not exist"));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new MyException("Wrong password");
        }

        String token = this.jwtService.buildToken(userMapper.toUserEntityForBuildToken(signInDto));


        return ApiResponse.<String>builder()
                .code(0)
                .message("Login successful")
                .success(true)
                .data(token)
                .build();
    }
}

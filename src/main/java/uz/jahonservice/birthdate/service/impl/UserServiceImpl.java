package uz.jahonservice.birthdate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.ErrorDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.entity.User;
import uz.jahonservice.birthdate.exceptions.DatabaseException;
import uz.jahonservice.birthdate.exceptions.MyException;
import uz.jahonservice.birthdate.repository.UserRepository;
import uz.jahonservice.birthdate.service.UserService;
import uz.jahonservice.birthdate.service.mapper.UserMapper;
import uz.jahonservice.birthdate.service.validation.UserInfoValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInfoValidator userInfoValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<UserDto> changePassword(String oldPassword, String newPassword, String email) {
        List<ErrorDto> errors = this.userInfoValidator.isValidPassword(newPassword);
        if (!errors.isEmpty()) {
            return ApiResponse.<UserDto>builder()
                    .code(-2)
                    .success(false)
                    .errorList(errors)
                    .build();
        }

        try {

            User user = this.userRepository.findByEmail(email).orElseThrow();

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new MyException("Password wrong");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            this.userRepository.save(user);

            return ApiResponse.<UserDto>builder()
                    .code(0)
                    .success(true)
                    .data(this.userMapper.toDto(user))
                    .build();

        } catch (Exception e) {
            throw new DatabaseException("Database exception while changing password");
        }
    }




    @Override
    public ApiResponse<UserDto> createUser(SignUpDto signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return ApiResponse.<UserDto>builder()
                    .code(-2)
                    .success(false)
                    .errorList(List.of(new ErrorDto("email", "This email already exists")))
                    .build();
        }

        try {
            return ApiResponse.<UserDto>builder()
                    .code(0)
                    .success(true)
                    .data(
                            userMapper.toDto(
                                    userRepository.save(
                                            userMapper.toUserEntity(signUpDto)
                                    )
                            )
                    )
                    .build();
        } catch (Exception e) {
            throw new DatabaseException("Database exception while creating user");
        }
    }

    @Override
    public ApiResponse<UserDto> deleteUser(String email) {
        if (email == null || email.isEmpty()) {
            return ApiResponse.<UserDto>builder()
                    .code(-2)
                    .success(false)
                    .errorList(List.of(new ErrorDto("email", "email cannot be null or empty")))
                    .build();
        }

       try {
           User user = userRepository.deleteByEmail(email).orElseThrow();

           return ApiResponse.<UserDto>builder()
                   .code(0)
                   .success(true)
                   .data(userMapper.toDto(user))
                   .build();
       }catch (Exception e){
           throw new DatabaseException("Database exception while deleting user");
       }

    }

    @Override
    public ApiResponse<UserDto> changeUserInfo(String firstName, String lastName, String newEmail, String email) {
        List<ErrorDto> errors = this.userInfoValidator.isValidEmail(newEmail);
        if (!errors.isEmpty()) {
            return ApiResponse.<UserDto>builder()
                    .code(-2)
                    .success(false)
                    .errorList(errors)
                    .build();
        }
      try {
          User user = userRepository.findByEmail(email).orElseThrow(() -> new MyException("User not found"));
          user.setFirstName(firstName);
          user.setLastName(lastName);
          user.setEmail(newEmail);
          userRepository.save(user);
          return ApiResponse.<UserDto>builder()
                  .code(0)
                  .success(true)
                  .data(userMapper.toDto(user))
                  .build();
      }catch (Exception e){
          throw new DatabaseException("Database exception while changing user info");
      }
    }

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {
        try {
            List<User> users = this.userRepository.findAll();
            List<UserDto> list = users.stream().map(userMapper::toDto).toList();
            return ApiResponse.<List<UserDto>>builder()
                    .code(0)
                    .success(true)
                    .data(list)
                    .build();
        }catch (Exception e){
            throw new DatabaseException("Database exception while getting all users");
        }

    }

    public Integer leftDays(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        birthDate = birthDate.withYear(LocalDate.now().getYear());
        if (birthDate.isBefore(now)) birthDate = birthDate.plusYears(1);
        long leftDays = ChronoUnit.DAYS.between(now, birthDate);
        return Integer.valueOf(Long.toString(leftDays));
    }

}

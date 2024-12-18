package uz.jahonservice.birthdate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.jahonservice.birthdate.dto.response.ApiResponse;
import uz.jahonservice.birthdate.dto.ErrorDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.dto.response.PageResponse;
import uz.jahonservice.birthdate.entity.User;
import uz.jahonservice.birthdate.exceptions.DatabaseException;
import uz.jahonservice.birthdate.exceptions.MyException;
import uz.jahonservice.birthdate.repository.UserRepository;
import uz.jahonservice.birthdate.service.UserService;
import uz.jahonservice.birthdate.service.mapper.UserMapper;
import uz.jahonservice.birthdate.service.validation.UserInfoValidator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
                    .user(this.userMapper.toDto(user))
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
                    .user(
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
                   .user(userMapper.toDto(user))
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
                  .user(userMapper.toDto(user))
                  .build();
      }catch (Exception e){
          throw new DatabaseException("Database exception while changing user info");
      }
    }

    @Override
    public PageResponse<List<UserDto>> getAllUsers(Integer size, Integer page, String pattern) {
        Pageable pageable = PageRequest.of(page, size);
        if (pattern == null){
            Page<User> pageUsers = userRepository.findAll(pageable);
            return PageResponse.<List<UserDto>>builder()
                    .code(0)
                    .message("Jura string null kelgan menga hamma userni qaytardim")
                    .page(pageUsers.getNumber())
                    .size(pageUsers.getSize())
                    .total(pageUsers.getTotalPages())
                    .list(pageUsers.map(userMapper::toDto).toList())
                    .build();
        }

        Page<User> users = userRepository.findByFirstNameContainingIgnoreCase(pattern, pageable);
        return PageResponse.<List<UserDto>>builder()
                .code(0)
                .message("uxshash userlar")
                .page(users.getNumber())
                .size(users.getSize())
                .total(users.getTotalPages())
                .list(users.map(userMapper::toDto).toList())
                .build();
    }


    public Integer leftDays(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        birthDate = birthDate.withYear(LocalDate.now().getYear());
        if (birthDate.isBefore(now)) birthDate = birthDate.plusYears(1);
        long leftDays = ChronoUnit.DAYS.between(now, birthDate);
        return Integer.valueOf(Long.toString(leftDays));
    }

}

package uz.jahonservice.birthdate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.jahonservice.birthdate.dto.UserUpdateDto;
import uz.jahonservice.birthdate.dto.response.ApiResponse;
import uz.jahonservice.birthdate.dto.ErrorDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.dto.response.PageResponse;
import uz.jahonservice.birthdate.entity.RoleEnum;
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
import java.util.stream.Collectors;

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
        } catch (Exception e) {
            throw new DatabaseException("Database exception while deleting user");
        }

    }

    @Override
    public ApiResponse<UserDto> changeUserInfo(UserUpdateDto userDto,  String email) {
        List<ErrorDto> errors = new ArrayList<>();
        if (userDto.getEmail()!= null) errors.addAll(this.userInfoValidator.isValidEmail(email));
        if (userDto.getBirthDate() != null) errors.addAll(userInfoValidator.isValidBirthday(userDto.getBirthDate()));
        if (!errors.isEmpty()) {
            return ApiResponse.<UserDto>builder()
                    .code(-2)
                    .success(false)
                    .errorList(errors)
                    .message("Validation error")
                    .build();
        }
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new MyException("This user does not exist"));
            return ApiResponse.<UserDto>builder()
                    .code(0)
                    .success(true)
                    .message("User updated")
                    .user(
                            userMapper.toDto(
                                    userRepository.save(
                                            userMapper.updatedUser(user, userDto)
                                    )
                            )
                    )
                    .build();
        }catch (Exception e) {
            throw new DatabaseException("Database exception while updating user");
        }
    }

//    @Override
//    public ApiResponse<UserDto> changeUserInfo(String firstName, String lastName, String newEmail, String email) {
//        List<ErrorDto> errors = this.userInfoValidator.isValidEmail(newEmail);
//        if (!errors.isEmpty()) {
//            return ApiResponse.<UserDto>builder()
//                    .code(-2)
//                    .success(false)
//                    .errorList(errors)
//                    .build();
//        }
//        try {
//            User user = userRepository.findByEmail(email).orElseThrow(() -> new MyException("User not found"));
//            user.setFirstName(firstName);
//            user.setLastName(lastName);
//            user.setEmail(newEmail);
//            userRepository.save(user);
//            return ApiResponse.<UserDto>builder()
//                    .code(0)
//                    .success(true)
//                    .user(userMapper.toDto(user))
//                    .build();
//        } catch (Exception e) {
//            throw new DatabaseException("Database exception while changing user info");
//        }
//    }

    @Override
    public PageResponse<List<UserDto>> getAllUsers(Integer size, Integer page, String pattern) {
        if (pattern == null) {
            List<User> users = removeAdmins(userRepository.findAll());
            List<User> pageUsers = new ArrayList<>();

            for (int i = (page - 1) * size; i < page * size && i < users.size(); i++) {
                pageUsers.add(users.get(i));
            }

            return PageResponse.<List<UserDto>>builder()
                    .code(0)
                    .success(true)
                    .message("Jura string null kelgan menga hamma userni qaytardim")
                    .page(page)
                    .size(size)
                    .total(users.size())
                    .users(pageUsers.stream().map(userMapper::toDto).toList())
                    .build();
        }

        List<User> users = removeAdmins(userRepository.findAllByFirstNameContainingIgnoreCase(pattern));
        List<User> pageUsers = new ArrayList<>();
        for (int i = (page - 1) * size; i < page * size && i < users.size(); i++) {
            pageUsers.add(users.get(i));
        }
        return PageResponse.<List<UserDto>>builder()
                .code(0)
                .success(true)
                .message(String.format("%s like users", pattern))
                .page(page)
                .size(size)
                .total(users.size())
                .users(pageUsers.stream().map(userMapper::toDto).toList())
                .build();
    }


    public Integer leftDays(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        birthDate = birthDate.withYear(LocalDate.now().getYear());
        if (birthDate.isBefore(now)) birthDate = birthDate.plusYears(1);
        long leftDays = ChronoUnit.DAYS.between(now, birthDate);
        return Integer.valueOf(Long.toString(leftDays));
    }

    public List<User> removeAdmins(List<User> users) {
        return users.stream()
                .filter(user -> !user.getRole().equals(RoleEnum.ADMIN))
                .collect(Collectors.toList());
    }


}

package uz.jahonservice.birthdate.service;

import org.springframework.data.domain.Page;
import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.exceptions.DatabaseException;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    ApiResponse<UserDto> changePassword(String oldPassword, String newPassword, String email) throws DatabaseException;

    ApiResponse<UserDto> createUser(SignUpDto signUpDto);

    ApiResponse<UserDto> deleteUser(String email);

    ApiResponse<UserDto> changeUserInfo(String firstName, String lastName, String newEmail, String email);

    ApiResponse<List<UserDto>> getAllUsers();

    ApiResponse<Page<UserDto>> getAllUserWithPagination(Integer page, Integer size);

    ApiResponse<Page<UserDto>> findUsers(Integer pageNumber, Integer size, String str);

    Integer leftDays(LocalDate birthDate);
}

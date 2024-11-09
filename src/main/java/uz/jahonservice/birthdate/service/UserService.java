package uz.jahonservice.birthdate.service;

import uz.jahonservice.birthdate.dto.response.ApiResponse;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.dto.response.PageResponse;
import uz.jahonservice.birthdate.exceptions.DatabaseException;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    ApiResponse<UserDto> changePassword(String oldPassword, String newPassword, String email) throws DatabaseException;

    ApiResponse<UserDto> createUser(SignUpDto signUpDto);

    ApiResponse<UserDto> deleteUser(String email);

    ApiResponse<UserDto> changeUserInfo(String firstName, String lastName, String newEmail, String email);

    PageResponse<List<UserDto>> getAllUsers(Integer size, Integer page, String pattern);

    Integer leftDays(LocalDate birthDate);

}

package uz.jahonservice.birthdate.service;

import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.exceptions.DatabaseException;

public interface UserService {
    ApiResponse<UserDto> changePassword(String oldPassword, String newPassword, String email) throws DatabaseException;

    ApiResponse<UserDto> changeEmail(String email, String newEmail);

    ApiResponse<UserDto> changeName(String firstName, String password, String email);

    ApiResponse<UserDto> changeLastName(String lastName, String password, String email);

    ApiResponse<UserDto> createUser(SignUpDto signUpDto);

    ApiResponse<UserDto> deleteUser(String email);
}

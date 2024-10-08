package uz.jahonservice.birthdate.service;

import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignInDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.exceptions.MyException;

public interface AuthService {

    ApiResponse<UserDto> registration(SignUpDto dto) throws MyException;

    ApiResponse<String> login(SignInDto signInDto) throws MyException;

}

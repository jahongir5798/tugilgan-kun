package uz.jahonservice.birthdate.service;

import uz.jahonservice.birthdate.dto.*;
import uz.jahonservice.birthdate.dto.response.JwtResponse;
import uz.jahonservice.birthdate.exceptions.MyException;

public interface AuthService {

    JwtResponse<UserDto> registration(SignUpDto dto) throws MyException;

    JwtResponse<UserDto> login(SignInDto signInDto) throws MyException;

}

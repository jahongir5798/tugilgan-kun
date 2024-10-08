package uz.jahonservice.birthdate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignInDto;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.exceptions.MyException;
import uz.jahonservice.birthdate.service.AuthService;

@RestController()
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;

    @PutMapping("/registration")
    public ApiResponse<UserDto> registration(
            @RequestBody @Valid SignUpDto dto
    ) {
        log.info("Auth controller registration method colled");
        ApiResponse<UserDto> registration = authService.registration(dto);
        log.info("Auth controller registration method response");
        return registration;
    }

    @GetMapping("/login")
    public ApiResponse<String> login(
            @RequestBody @Valid SignInDto signInDto
    ) {
        log.info("Auth controller login method colled");
        ApiResponse<String> login = this.authService.login(signInDto);
        log.info("Auth controller login method response");
        return login;
    }


}

package uz.jahonservice.birthdate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import uz.jahonservice.birthdate.dto.*;
import uz.jahonservice.birthdate.service.AuthService;

@RestController()
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public JwtResponse<UserDto> registration(
            @RequestBody @Valid SignUpDto dto
    ) {
        log.info("Auth controller registration method colled");
        JwtResponse<UserDto> registration = authService.registration(dto);
        log.info("Auth controller registration method response");
        return registration;
    }

    @PostMapping("/login")
    public JwtResponse<UserDto> login(
            @RequestBody @Valid SignInDto signInDto
    ) {
        log.info("Auth controller login method colled");
        JwtResponse<UserDto> login = this.authService.login(signInDto);
        log.info("Auth controller login method response");
        return login;
    }


}

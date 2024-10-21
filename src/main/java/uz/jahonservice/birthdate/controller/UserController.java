package uz.jahonservice.birthdate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.jahonservice.birthdate.dto.ApiResponse;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.service.UserService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserController {

    private final UserService userService;

    @PatchMapping("/change/password")
   // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") todo: meningcha role bu methodga kerak emas, chunki hamma uzini passwordini uzgartirishga haqqi bor
    public ApiResponse<UserDto> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String email
    ) {
        return userService.changePassword(oldPassword, newPassword, email);
    }

    @PutMapping("/change/userInfo")
    public ApiResponse<UserDto> changeSerInfo(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String newEmail,
            @RequestParam String email
    ){
        log.info("UserController changeInfo method called");
        ApiResponse<UserDto> userDtoApiResponse = userService.changeUserInfo(firstName, lastName, newEmail, email);
        log.info("User controller changeInfo method response: {}", userDtoApiResponse);
        return userDtoApiResponse;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDto> createUser(@RequestBody @Valid SignUpDto signUpDto) {
        log.info("User controller create user method called");
        ApiResponse<UserDto> user = userService.createUser(signUpDto);
        log.info("User controller create user method response: {}", user);
        return user;
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDto> deleteUser(@RequestParam @NotNull String email) {
        log.info("User controller delete user method called");
        ApiResponse<UserDto> deleteUser = userService.deleteUser(email);
        log.info("User controller delete user method response: {}", deleteUser);
        return deleteUser;
    }



    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserDto>> getAllUsers() {
        log.info("User controller getAllUsers method called");
        ApiResponse<List<UserDto>> allUsers = userService.getAllUsers();
        log.info("User controller getAllUsers method response: {}", allUsers);
        return allUsers;
    }

    @GetMapping("/allUsers/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<UserDto>> getAllUserWithPagination(
            @RequestParam Integer pageNumber,
            @RequestParam Integer size
    ){
        log.info("User controller getAllUserWithPagination method called");
        ApiResponse<Page<UserDto>> result = userService.getAllUserWithPagination(pageNumber, size);
        log.info("User controller getAllUserWithPagination method response: {}", result);
        return result;
    }

    @GetMapping("/allUsers/findByName")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<UserDto>> findUsers(
            @RequestParam Integer pageNumber,
            @RequestParam Integer size,
            @RequestParam String firstName
    ){
        log.info("User controller findUsers method called");
        ApiResponse<Page<UserDto>> result = userService.findUsers(pageNumber, size, firstName);
        log.info("User controller findUsers method response: {}", result);
        return result;
    }




}

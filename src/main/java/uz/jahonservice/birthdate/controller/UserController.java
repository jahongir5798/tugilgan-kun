package uz.jahonservice.birthdate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.jahonservice.birthdate.dto.UserUpdateDto;
import uz.jahonservice.birthdate.dto.response.ApiResponse;
import uz.jahonservice.birthdate.dto.SignUpDto;
import uz.jahonservice.birthdate.dto.UserDto;
import uz.jahonservice.birthdate.dto.response.PageResponse;
import uz.jahonservice.birthdate.service.UserService;

import java.time.LocalDate;
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

    @PutMapping("/change-user-info/{email}")
    public ApiResponse<UserDto> changeSerInfo(
           @RequestBody UserUpdateDto userDto,
            @PathVariable String email
    ){
        log.info("UserController changeInfo method called");
        ApiResponse<UserDto> userDtoApiResponse = userService.changeUserInfo(userDto, email);
        log.info("User controller changeInfo method response: {}", userDtoApiResponse);
        return userDtoApiResponse;
    }



    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDto> deleteUser(@RequestParam @NotNull String email) {
        log.info("User controller delete user method called");
        ApiResponse<UserDto> deleteUser = userService.deleteUser(email);
        log.info("User controller delete user method response: {}", deleteUser);
        return deleteUser;
    }



    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserDto>> getAllUsers(
            @RequestParam Integer size,
            @RequestParam Integer page,
            @RequestParam (required = false) String userName
    ) {
        log.info("User controller getAllUsers method called");
        PageResponse<List<UserDto>> allUsers = userService.getAllUsers(size, page, userName);
        log.info("User controller getAllUsers method response: {}", allUsers);
        return allUsers;
    }








}

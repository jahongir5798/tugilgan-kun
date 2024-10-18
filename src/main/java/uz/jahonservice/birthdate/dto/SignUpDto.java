package uz.jahonservice.birthdate.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String prePassword;

    @PastOrPresent
    private LocalDate birthDate;


}

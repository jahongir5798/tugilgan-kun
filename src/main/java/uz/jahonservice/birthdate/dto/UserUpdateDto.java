package uz.jahonservice.birthdate.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    private String email;// bu yangi email

    private LocalDate birthDate;

}

package uz.jahonservice.birthdate.dto;

import lombok.*;
import uz.jahonservice.birthdate.entity.RoleEnum;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private RoleEnum role;

    private LocalDate birthDate;

    private Integer untilBirthDate;

}

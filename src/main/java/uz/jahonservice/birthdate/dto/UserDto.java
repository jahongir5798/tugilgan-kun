package uz.jahonservice.birthdate.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstname;

    private String lastname;

    private LocalDate birthDate;

    private Integer untilBirthDate;

}

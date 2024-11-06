package uz.jahonservice.birthdate.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Integer untilBirthDate;

}

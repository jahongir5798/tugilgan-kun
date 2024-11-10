package uz.jahonservice.birthdate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JwtResponse <T> {

    int code;

    boolean success;

    String message;

    String token;

    T user;

}

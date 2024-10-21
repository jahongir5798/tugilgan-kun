package uz.jahonservice.birthdate.dto;

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

    T data;

}

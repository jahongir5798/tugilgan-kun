package uz.jahonservice.birthdate.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int code;  //todo: -2 validate error; -3 database exception

    private String message;

    private boolean success;

    private T user;

    private List<ErrorDto> errorList;

}

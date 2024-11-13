package uz.jahonservice.birthdate.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse <T>{

    private int code;

    private boolean success;

    private String message;

    private int page;

    private int size;

    private int total;

    private T users;

}

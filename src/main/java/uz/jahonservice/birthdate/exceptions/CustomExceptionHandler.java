package uz.jahonservice.birthdate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.jahonservice.birthdate.dto.response.ApiResponse;
import uz.jahonservice.birthdate.dto.ErrorDto;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {


        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ErrorDto> errors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            String rejectedValue = String.valueOf(fieldError.getRejectedValue());

            errors.add(new ErrorDto(field, String.format("%s, rejected value: %s", message, rejectedValue)));
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.<Void>builder()
                        .code(-2)
                        .message("Validation Failed")
                        .errorList(errors)
                        .build()
        );
    }

    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<ApiResponse<Void>> MyException(MyException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.<Void>builder()
                        .code(-1)
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = DatabaseException.class)
    public ResponseEntity<ApiResponse<Void>> databaseException(DatabaseException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.<Void>builder()
                        .code(-3)
                        .message(e.getMessage())
                        .build()
        );
    }
}






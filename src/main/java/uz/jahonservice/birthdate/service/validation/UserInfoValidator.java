package uz.jahonservice.birthdate.service.validation;

import org.springframework.stereotype.Component;
import uz.jahonservice.birthdate.dto.ErrorDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserInfoValidator {

    public List<ErrorDto> isValidPassword(String password){
        List<ErrorDto> errors = new ArrayList<>();
        if (Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$", password)){
            errors.add(new ErrorDto("password", "Password is weak"));
        }
        return errors;
    }

    public List<ErrorDto> isValidEmail(String email){
        List<ErrorDto> errors = new ArrayList<>();
        if (Pattern.matches( "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",email)){
            errors.add(new ErrorDto("email", "Email is incorext"));
        }
        return errors;
    }
}

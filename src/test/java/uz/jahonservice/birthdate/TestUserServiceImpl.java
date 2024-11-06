package uz.jahonservice.birthdate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.jahonservice.birthdate.service.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    //todo: unit tests
    @Test
    void testLeftDaysAfterDays(){
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(10);

        Integer actual = userService.leftDays(localDate);

        Assertions.assertEquals(10, actual);
    }

    @Test
    void testLeftDaysBeforeDays(){
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(10).plusYears(1);

        Integer actual = userService.leftDays(localDate);
        Integer expected = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), localDate));

        Assertions.assertEquals(expected, actual);
    }

}

package yandex.practicum.controller;

import org.junit.Test;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class UserControllerTest {
    private final UserController controller = new UserController();
    private final User user1 = new User("floppy-poppy", "Poppy", "poppy@gmail.com", LocalDate.of(1999, 12, 12));
    private final User user2 = new User("holly-polly", "Polly", "polly@gmail.com", LocalDate.of(1999, 12, 12));

    @Test
    public void createAndGet() {
        controller.create(user1);
        controller.create(user2);
        assertEquals(controller.getAll().size(), 2);
    }

    @Test
    public void shouldTestEmail() {
        User user3 = new User("hey", "folly-molly", "mollyGmail.com@", LocalDate.of(1999, 12, 12));
        controller.create(user3);

    }


    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenEmailIsInvalid() {
        User user = new User();
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setEmail("invalid-email");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        controller.create(user);
    }


    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionAtBlankNameFormat() {
        User user3 = new User("", "folly-molly", "molly@gmail.com", LocalDate.of(1999, 12, 12));
        controller.create(user3);
    }

    @Test
    public void shouldPutLoginIfNameIsVoid() {
        User user3 = new User("folly-molly", "", "molly@gmail.com", LocalDate.of(1999, 12, 12));
        controller.create(user3);
        assertEquals(user3.getLogin(), user3.getName());
    }

    @Test(expected = ValidationException.class)
    public void shouldFailFutureBirthday() {
        User user3 = new User("folly-molly", "", "molly@gmail.com", LocalDate.of(2025, 12, 12));
        controller.create(user3);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailEmailFormat() {
        User user3 = new User("folly-molly", "", "mollyGmail.com", LocalDate.of(1999, 12, 12));
        controller.create(user3);
    }

    @Test
    public void update() {
        User user3 = new User("Dolly", "folly-molly", "molly@gmail.com", LocalDate.of(1999, 12, 12));
        controller.create(user3);
        user3.setLogin("Molly");
        controller.update(user3);
        assertEquals("Molly", user3.getLogin());
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionForNullUser() {
        controller.create(null);
    }

}
package yandex.practicum.controller;

import org.junit.Test;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

public class FilmControllerTest {
    private final FilmController controller = new FilmController();
    private final Film film1 = new Film("sampleName1", "sampleDescription", LocalDate.of(2000, 12, 12), Duration.of(60, ChronoUnit.MINUTES));
    private final Film film2 = new Film("sampleName2", "sampleDescription", LocalDate.of(2000, 12, 12), Duration.of(60, ChronoUnit.MINUTES));


    @Test
    public void addAndGet() {
        controller.add(film1);
        controller.add(film2);
        assertEquals(controller.getAll().size(), 2);
    }

    @Test
    public void update() {
        controller.add(film1);
        film1.setName("newName");
        controller.update(film1);
        assertEquals(film1.getName(), "newName");
    }

    @Test(expected = ValidationException.class)
    public void shouldFailIfDescriptionIsMoreThan200() {
        Film film3 = new Film("sampleName2",
                "sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription sampleDescription",
                LocalDate.of(2000, 12, 12), Duration.of(60, ChronoUnit.MINUTES));
        controller.add(film3);

    }

    @Test(expected = ValidationException.class)
    public void shouldFailIfNameIsVoid() {
        Film film3 = new Film("", "sampleDescription", LocalDate.of(2000, 12, 12), Duration.of(60, ChronoUnit.MINUTES));
        controller.add(film3);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailIfDurationIsNegative() {
        Film film3 = new Film("sampleName2", "sampleDescription", LocalDate.of(2000, 12, 12), Duration.of(-60, ChronoUnit.MINUTES));
        controller.add(film3);
    }

    @Test(expected = ValidationException.class)
    public void shouldFailIfReleaseIsBeforeGiven() {
        Film film3 = new Film("", "sampleDescription", LocalDate.of(1895, 12, 12), Duration.of(60, ChronoUnit.MINUTES));
        controller.add(film3);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionForNullUser() {
        controller.add(null);
    }


    @Test(expected = ValidationException.class)
    public void shouldCheckValidation() throws NoSuchMethodException {
        Method privateMethod = FilmController.class.getDeclaredMethod("validate", Film.class);
        privateMethod.setAccessible(true);
        Film film3 = new Film("sampleName2", "sampleDescription", LocalDate.of(2000, 12, 12), Duration.of(-60, ChronoUnit.MINUTES));
        controller.add(film3);
    }

}
package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("начало валидации фильма при создании {}", film);
        if (validate(film)) {
            film.setId(getNextId());
            log.info("создание фильма ID {} ", film.getId());
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return Optional.ofNullable(films.get(newFilm.getId())).map(oldFilm -> {
            log.info("валидация фильма ID {} при обновлении", newFilm.getId());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setName(newFilm.getName());
            log.info("обновление фильма ID {}", newFilm.getId());
            return oldFilm;
        }).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    private boolean validate(Film film) {
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().toCharArray().length > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов, а у вас " + film.getDescription().toCharArray().length);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException(String.format("Дата релиза %s недопустима", film.getReleaseDate()));
        }

        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        return true;
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

}


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
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        log.info("валидация пользователя при создании: {}", user);
        user.setId(getNextId());
        log.info("добавление пользователя ID {}", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return Optional.ofNullable(users.get(newUser.getId())).map(oldUser -> {
            log.info("валидация пользователья ID {} при обновлении", newUser.getId());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("обновление пользователя ID {}", newUser.getId());
            return oldUser;
        }).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("получение всех пользователей");
        return users.values();
    }

    private boolean validate(User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть пустым");
        }
        if (user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Ошибка формата электронной почты");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка формата логина");
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка формата даты рождения");
        }
        return true;

    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

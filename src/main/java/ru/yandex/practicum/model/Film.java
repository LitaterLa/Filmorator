package ru.yandex.practicum.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Film {
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

package com.spring.songify.song.infrastructure.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SongRequestDto(
        @NotNull(message = "songName can't be null!")
        @NotEmpty(message = "songName can't be empty!")
        String songName,
        @NotNull(message = "artist can't be null!")
        @NotEmpty(message = "artist can't be empty!")
        String artist) {
}

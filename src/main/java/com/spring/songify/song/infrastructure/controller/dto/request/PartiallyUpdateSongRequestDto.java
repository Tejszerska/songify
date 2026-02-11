package com.spring.songify.song.infrastructure.controller.dto.request;

public record PartiallyUpdateSongRequestDto(
        String songName,
        String artist
) {
}

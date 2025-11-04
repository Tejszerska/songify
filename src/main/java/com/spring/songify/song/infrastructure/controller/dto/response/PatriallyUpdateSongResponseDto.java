package com.spring.songify.song.infrastructure.controller.dto.response;

import com.spring.songify.song.domain.model.Song;

public record PatriallyUpdateSongResponseDto(Song updatedSong) {
}

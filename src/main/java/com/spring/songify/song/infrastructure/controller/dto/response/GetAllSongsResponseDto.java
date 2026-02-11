package com.spring.songify.song.infrastructure.controller.dto.response;


import com.spring.songify.song.domain.model.Song;

import java.util.Map;

public record GetAllSongsResponseDto(Map<Integer, Song> songs) {
}

package com.spring.songify.song.domain.service;

import com.spring.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.response.CreateSongResponseDto;
import com.spring.songify.song.domain.model.Song;

public class SongMapper {
    public static Song mapFromCreateSongRequestDtoToSong(CreateSongRequestDto songRequest) {
        return  new Song(songRequest.songName(), songRequest.artist());
    }

    public static CreateSongResponseDto mapFromSongToCreateSongResponseDto(Song song) {
        return new CreateSongResponseDto(song);
    }
}

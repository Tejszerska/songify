package com.spring.songify.song.infrastructure.controller;

import com.spring.songify.song.domain.service.SongAdder;
import com.spring.songify.song.domain.service.SongRetriever;
import com.spring.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.response.*;
import com.spring.songify.song.domain.model.SongNotFoundException;
import com.spring.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/songs")
@RestController
public class SongRestController {
    private final SongAdder songAdder;
    private final SongRetriever songRetriever;



    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, Song> limitedMap = songRetriever.findAllLimitedBy(limit);
            GetAllSongsResponseDto response = new GetAllSongsResponseDto(limitedMap);
            return ResponseEntity.ok(response);
        }
        Map<Integer, Song> allSongs = songRetriever.findAll();
        GetAllSongsResponseDto response = SongMapper.mapFromSongToGetAllSongsResponseDto(allSongs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Integer id, @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        Map<Integer, Song> allSongs = songRetriever.findAll();

        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song song = allSongs.get(id);
        GetSongResponseDto response = SongMapper.mapFromSongToGetSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = SongMapper.mapFromCreateSongRequestDtoToSong(request);
        songAdder.addSong(song);
        CreateSongResponseDto body = SongMapper.mapFromSongToCreateSongResponseDto(song);
        return ResponseEntity.ok(body);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        allSongs.remove(id);
        DeleteSongResponseDto body = SongMapper.mapFromSongToDeleteSongResponseDto(id);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> update(@PathVariable Integer id,
                                                        @RequestBody @Valid UpdateSongRequestDto request) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song newSong = SongMapper.mapFromUpdateSongRequestDtoToSong(request);
        Song oldSong = allSongs.put(id, newSong);
        log.info("Updated song with id: " + id +
                " with oldSongName: " + oldSong.name() + " to newSongName: " + newSong.name() +
                " oldArtist: " + oldSong.artist() + " to newArtist: " + newSong.artist());
        UpdateSongResponseDto body = SongMapper.mapFromSongToUpdateSongResponseDto(newSong);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(@PathVariable Integer id,
                                                                              @RequestBody PartiallyUpdateSongRequestDto request) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song songFromDatabase = allSongs.get(id);
        Song updatedSong = SongMapper.mapFromPartiallyUpdateSongRequestDtoToSong(request);
        Song.SongBuilder builder = Song.builder();
        if (updatedSong.name() != null) {
            builder.name(updatedSong.name());
        } else {
            builder.name(songFromDatabase.name());
        }
        if (updatedSong.artist() != null) {
            builder.artist(updatedSong.artist());
        } else {
            builder.artist(songFromDatabase.artist());
        }
        allSongs.put(id, updatedSong);
        PartiallyUpdateSongResponseDto body = SongMapper.mapFromSongToPartiallyUpdateSongResponseDto(updatedSong);
        return ResponseEntity.ok(body);
    }
}

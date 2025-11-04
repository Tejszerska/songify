package com.spring.songify.song.infrastructure.controller;

import com.spring.songify.song.domain.service.SongMapper;
import com.spring.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.request.PatriallyUpdateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.spring.songify.song.infrastructure.controller.dto.response.*;
import com.spring.songify.song.domain.model.SongNotFoundException;
import com.spring.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/songs")
public class SongRestController {
    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Hells Bells", "AC/DC"),
            2, new Song("Yellow Submarine", "The Beatles"),
            3, new Song("Shallow", "Lady Gaga & Bradley Cooper"),
            4, new Song("Stitches", "Shawn Mendes")));

    @GetMapping
    public ResponseEntity<GetAllSongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {

        if (limit != null) {
            Map<Integer, Song> limitedMap = database.entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            GetAllSongResponseDto songResponseDTO = new GetAllSongResponseDto(limitedMap);
            return ResponseEntity.ok(songResponseDTO);
        }
        GetAllSongResponseDto songResponseDTO = new GetAllSongResponseDto(database);
        return ResponseEntity.ok(songResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Integer id,
                                                          @RequestHeader(required = false) String requestId) {
        log.info("Header requestId is "+ requestId);
        checkIfPresentInDatabase(id);
        Song song = database.get(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        GetSongResponseDto singleSongResponseDTO = new GetSongResponseDto(song);
        return ResponseEntity.ok(singleSongResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto songRequest) {
        Song song = SongMapper.mapFromCreateSongRequestDtoToSong(songRequest);
        int id = database.size() + 1;
        database.put(id, song);
        log.info("Song by name: " + song + "added with id: " + id);
        CreateSongResponseDto responseDto = SongMapper.mapFromSongToCreateSongResponseDto(song);
        return ResponseEntity.ok(responseDto);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        checkIfPresentInDatabase(id);

        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto(("Song deleted by id: " + id), HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponse> update(@PathVariable Integer id,
                                                     @RequestBody @Valid UpdateSongRequestDto request) {
        checkIfPresentInDatabase(id);

        String newSongName = request.songName();
        String newArtist = request.artist();
        Song newSong = new Song(newSongName, newArtist);
        Song oldSong = database.put(id, newSong);
        log.info("Song updated succesfully \\/ "
                + "\n id: " + id
                + "\n old song: " + oldSong
                + "\n new song: " + newSong
        );
        return ResponseEntity.ok(new UpdateSongResponse(newSongName, newArtist));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatriallyUpdateSongResponseDto> partiallyUpdateSong(@PathVariable Integer id,
                                                                              @RequestBody PatriallyUpdateSongRequestDto request) {
        checkIfPresentInDatabase(id);

        String newSongName = request.songName();
        String newArtist = request.artist();

        Song oldSong = database.get(id);
        Song.SongBuilder builder = Song.builder();

        if (newSongName != null) builder.name(newSongName);
        else builder.name(oldSong.name());

        if (newArtist != null) builder.artist(newArtist);
        else builder.artist(oldSong.artist());

        Song newSong = builder.build();
        database.put(id, newSong);
        log.info("Patrially updated song with id: " + id + " is now " + newSong);

        return ResponseEntity.ok(new PatriallyUpdateSongResponseDto(newSong));

    }

    private void checkIfPresentInDatabase(Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("No song with id=" + id + " in the database");
        }

    }
}

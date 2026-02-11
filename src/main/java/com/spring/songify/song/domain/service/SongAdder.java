package com.spring.songify.song.domain.service;

import com.spring.songify.song.domain.model.Song;
import com.spring.songify.song.domain.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SongAdder {
    private final SongRepository songRepository;
    public Song addSong(Song song) {
        // 2. Warstwa logiki biznesowej/serwisów domenowych: wyswietlamy informacje
        return songRepository.saveToDatabase(song);

    }

}

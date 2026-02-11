package com.spring.songify.song.domain.service;

import com.spring.songify.song.domain.model.Song;
import com.spring.songify.song.domain.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SongRetriever {
    private final SongRepository songRepository;

    public Map<Integer, Song> findAll(){
      log.info(">> Retrieving all songs from DB <<");
        return  songRepository.findAll();
    }
    public Map<Integer, Song> findAllLimitedBy(Integer limit){
      log.info(">> Retrieving " + limit + " songs from DB <<");
        return  songRepository.findAll().entrySet()
                .stream()
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}

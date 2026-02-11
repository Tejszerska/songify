package com.spring.songify.song.domain.repository;

import com.spring.songify.song.domain.model.Song;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SongRepository {
    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Hells Bells", "AC/DC"),
            2, new Song("Yellow Submarine", "The Beatles"),
            3, new Song("Shallow", "Lady Gaga & Bradley Cooper"),
            4, new Song("Stitches", "Shawn Mendes")));
     public Song saveToDatabase(Song song) {
        // 3. Warstwę bazodanową: zapisujemy do bazy danych
        database.put(database.size() + 1, song);
        return song;
    }

    public Map<Integer, Song> findAll(){
         return database;
    }
}

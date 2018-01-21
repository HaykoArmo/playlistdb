package com.example.playlistdb;

import java.util.List;
import java.util.Map;

public interface SQLiteDAO {
    int insert(MP3 mp3);

    int insertList(List<MP3> mp3List);

    void delete(MP3 mp3);

    void delete(int id);

    MP3 getMP3ByID(int id);

    List<MP3> getMP3ListByName(String name);

    List<MP3> getMP3ListByAuthor(String author);

    int getMP3Count();

    Map<String, Integer> getStat();
}

package com.robinhowlett.parsers.tracks;

import com.robinhowlett.data.Track;

import java.util.List;
import java.util.Optional;

public interface TrackRepository {

    Optional<Track> findByCode(String trackCode);

    Optional<Track> findByName(String trackName);

    List<Track> findByState(String state);

    List<Track> findAll();
}

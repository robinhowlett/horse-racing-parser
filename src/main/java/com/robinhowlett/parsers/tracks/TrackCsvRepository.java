package com.robinhowlett.parsers.tracks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.robinhowlett.data.Track;
import com.robinhowlett.ser.SimpleLocalDateDeserializer;
import com.robinhowlett.ser.SimpleLocalDateSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Loads the {@link Track} codes, countries, and names from a CSV file
 */
public class TrackCsvRepository implements TrackRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackCsvRepository.class);
    private static final String FILENAME = "track-codes.csv";

    private final CsvMapper csvMapper;

    public TrackCsvRepository() {
        this.csvMapper = new TrackCsvMapper();
    }

    public TrackCsvRepository(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    @Override
    public Optional<Track> findByCode(String trackCode) {
        for (Track track : findAll()) {
            if (track.getCode().trim().equals(trackCode.trim())) {
                return of(track);
            }
        }
        return empty();
    }

    @Override
    public Optional<Track> findByName(String trackName) {
        for (Track track : findAll()) {
            if (track.getName().trim().equals(trackName.trim())) {
                return of(track);
            }
        }
        return empty();
    }

    @Override
    public List<Track> findByState(String state) {
        List<Track> tracks = new ArrayList<>();
        for (Track track : findAll()) {
            if (track.getState().trim().equals(state.trim())) {
                tracks.add(track);
            }
        }
        return tracks;
    }

    @Override
    public List<Track> findAll() {
        CsvSchema schema = CsvSchema.emptySchema().withColumnSeparator(';').withHeader();

        try {
            try (InputStream tracks = getClass().getClassLoader().getResourceAsStream(FILENAME)) {
                MappingIterator<Track> mappingIterator = csvMapper.readerFor(Track.class)
                        .with(schema).readValues(tracks);
                return mappingIterator.readAll();
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read %s as CSV", FILENAME),
                    e);
        }
    }

    static class TrackCsvMapper extends CsvMapper {
        public TrackCsvMapper() {
            super();

            disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

            // adds JDK 8 Parameter Name access for cleaner JSON-to-Object mapping
            registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

            SimpleModule simpleLocalDateModule = new SimpleModule();
            simpleLocalDateModule.addSerializer(LocalDate.class, new SimpleLocalDateSerializer());
            simpleLocalDateModule.addDeserializer(LocalDate.class, new
                    SimpleLocalDateDeserializer());
            registerModule(simpleLocalDateModule);
        }
    }
}

package dev.nos.djnavigator.analysis.listener;

import dev.nos.djnavigator.analysis.model.converter.AudioAnalysisFromSpotifyAudioAnalysisConverter;
import dev.nos.djnavigator.analysis.repository.AudioAnalysisRepository;
import dev.nos.djnavigator.collection.model.event.TrackCreatedEvent;
import dev.nos.djnavigator.event.EventListener;
import dev.nos.djnavigator.spotify.client.SpotifyQueriesAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.utils.ConvertersUtils.toSpotifyTrackId;
import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class GetAudioAnalysisOnTrackAddedEventListener implements EventListener<TrackCreatedEvent> {

    private final SpotifyQueriesAsync spotifyQueriesAsync;
    private final AudioAnalysisFromSpotifyAudioAnalysisConverter audioAnalysisConverter;
    private final AudioAnalysisRepository audioAnalysisRepository;

    @Autowired
    public GetAudioAnalysisOnTrackAddedEventListener(SpotifyQueriesAsync spotifyQueriesAsync,
                                                     AudioAnalysisFromSpotifyAudioAnalysisConverter audioAnalysisConverter,
                                                     AudioAnalysisRepository audioAnalysisRepository) {
        this.spotifyQueriesAsync = spotifyQueriesAsync;
        this.audioAnalysisConverter = audioAnalysisConverter;
        this.audioAnalysisRepository = audioAnalysisRepository;
    }

    @Override
    public void onEvent(TrackCreatedEvent event) {
        log.info(String.format("Listener %s received the event %s", getClass().getName(), event.type()));
        ofNullable(event.track().getSpotifyId())
                .ifPresent(spotifyId ->
                        spotifyQueriesAsync
                                .audioAnalysis(toSpotifyTrackId(spotifyId))
                                .thenApply(audioAnalysisConverter)
                                .thenApply(audioAnalysisRepository::save)
                );
    }
}

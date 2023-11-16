package dev.nos.djnavigator.analysis.model.converter;

import dev.nos.djnavigator.analysis.model.AudioAnalysis;
import dev.nos.djnavigator.spotify.model.SpotifyAudioAnalysis;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class AudioAnalysisFromSpotifyAudioAnalysisConverter implements Function<SpotifyAudioAnalysis, AudioAnalysis> {

    public final Clock clock;

    @Autowired
    public AudioAnalysisFromSpotifyAudioAnalysisConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public AudioAnalysis apply(SpotifyAudioAnalysis spotifyAudioAnalysis) {
        return AudioAnalysis.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .data(spotifyAudioAnalysis.audioAnalysis().toPrettyString())
                .build();
    }

}

package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.dto.MixCreateDto;
import dev.nos.djnavigator.collection.model.Mix;
import dev.nos.djnavigator.collection.model.Turntable;
import dev.nos.djnavigator.collection.repository.TrackRepository;
import dev.nos.djnavigator.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class MixConverter {

    public final Clock clock;
    private final TrackRepository trackRepository;

    @Autowired
    public MixConverter(Clock clock, TrackRepository trackRepository) {
        this.clock = clock;
        this.trackRepository = trackRepository;
    }


    public Mix createMix(MixCreateDto mixCreateDto) {
        final var leftTrack = trackRepository.findById(mixCreateDto.leftTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.leftTrackId()));
        final var rightTrack = trackRepository.findById(mixCreateDto.rightTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.rightTrackId()));

        return Mix.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .leftTurntable(
                        Turntable.builder()
                                .track(leftTrack)
                                .pitch(mixCreateDto.leftTrackPitch().orElse(new BigDecimal("0.0")))
                                .build()
                )
                .rightTurntable(
                        Turntable.builder()
                                .track(rightTrack)
                                .pitch(MixPitchCalculator.calculatePitch(leftTrack, rightTrack))
                                .build()
                )
                .build();
    }

}

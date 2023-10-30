package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.dto.MixCreateDto;
import dev.nos.djnavigator.collection.model.Mix;
import dev.nos.djnavigator.collection.model.Turntable;
import dev.nos.djnavigator.collection.repository.TrackRepository;
import org.springframework.stereotype.Service;
import org.unbrokendome.base62.Base62;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.UUID.randomUUID;

@Service
public class MixConverter {

    private final TrackRepository trackRepository;

    public MixConverter(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }


    public Mix createMix(MixCreateDto mixCreateDto) {
        final var leftTrack = trackRepository.findById(mixCreateDto.leftTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.leftTrackId()));
        final var rightTrack = trackRepository.findById(mixCreateDto.rightTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.rightTrackId()));

        return Mix.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
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

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

}

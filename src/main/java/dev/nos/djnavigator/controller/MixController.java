package dev.nos.djnavigator.controller;

import dev.nos.djnavigator.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.dto.MixCreateDto;
import dev.nos.djnavigator.dto.MixView;
import dev.nos.djnavigator.dto.TurntableView;
import dev.nos.djnavigator.model.Mix;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.model.Turntable;
import dev.nos.djnavigator.repository.MixRepository;
import dev.nos.djnavigator.repository.TrackRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unbrokendome.base62.Base62;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Comparator.naturalOrder;
import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/api")
public class MixController {

    private final MixRepository mixRepository;
    private final TrackRepository trackRepository;

    public MixController(MixRepository mixRepository, TrackRepository trackRepository) {
        this.mixRepository = mixRepository;
        this.trackRepository = trackRepository;
    }

    @PostMapping("/mixes")
    public MixView addAlbum(@RequestBody @Validated MixCreateDto mixCreateDto) {

        final var leftTrack = trackRepository.findById(mixCreateDto.leftTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.leftTrackId()));
        final var rightTrack = trackRepository.findById(mixCreateDto.rightTrackId())
                .orElseThrow(() -> new TrackNotFoundException(mixCreateDto.rightTrackId()));

        final var mix = mixRepository.save(
                Mix.builder()
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
                                        .pitch(calculatePitch(leftTrack, rightTrack))
                                        .build()
                        )
                        .build()
        );

        return toMixView(mix);
    }

    private MixView toMixView(Mix mix) {
        return MixView.builder()
                .leftTurntable(
                        TurntableView.builder()
                                .trackId(mix.getLeftTurntable().getTrack().getId())
                                .trackName(mix.getLeftTurntable().getTrack().getName())
                                .pitch(mix.getLeftTurntable().getPitch())
                                .build()
                )
                .rightTurntable(
                        TurntableView.builder()
                                .trackId(mix.getRightTurntable().getTrack().getId())
                                .trackName(mix.getRightTurntable().getTrack().getName())
                                .pitch(mix.getRightTurntable().getPitch())
                                .build()
                )
                .build();
    }

    private BigDecimal calculatePitch(Track staticTrack, Track dynamicTrack) {
        return levelledStaticTrackTempo(staticTrack.getTempo(), dynamicTrack.getTempo())
                .subtract(dynamicTrack.getTempo())
                .multiply(valueOf(100))
                .divide(dynamicTrack.getTempo(), HALF_UP)
                .setScale(1, HALF_UP);
    }

    private BigDecimal levelledStaticTrackTempo(BigDecimal staticVal, BigDecimal dynamicVal) {

        final var defaultRatio = ratio(dynamicVal, staticVal);

        final var multipliedStaticVal = staticVal.multiply(valueOf(2));
        final var multipliedRatio = ratio(dynamicVal, multipliedStaticVal);

        final var dividedStaticVal = staticVal.divide(valueOf(2), HALF_UP);
        final var dividedRatio = ratio(dynamicVal, dividedStaticVal);

        final var minRatio = Stream.of(defaultRatio, multipliedRatio, dividedRatio)
                .min(naturalOrder())
                .get();

        final var ratioMap = Map.of(
                defaultRatio, staticVal,
                multipliedRatio, multipliedStaticVal,
                dividedRatio, dividedStaticVal
        );

        return ratioMap.get(minRatio);
    }

    private static BigDecimal ratio(BigDecimal val1, BigDecimal val2) {
        return val2.doubleValue() > val1.doubleValue()
                ? val2.divide(val1, HALF_UP)
                : val1.divide(val2, HALF_UP);
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }
}

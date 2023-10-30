package dev.nos.djnavigator.collection.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.nos.djnavigator.collection.controller.converter.model.MixConverter;
import dev.nos.djnavigator.collection.controller.converter.model.MixPitchCalculator;
import dev.nos.djnavigator.collection.controller.converter.view.MixViewConverter;
import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.dto.MixCreateDto;
import dev.nos.djnavigator.collection.dto.MixView;
import dev.nos.djnavigator.collection.model.AlbumId;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.repository.AlbumRepository;
import dev.nos.djnavigator.collection.repository.MixRepository;
import dev.nos.djnavigator.collection.repository.TrackRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/api")
public class MixController {

    private final MixRepository mixRepository;
    private final TrackRepository trackRepository;
    private final MixConverter mixConverter;
    private final AlbumRepository albumRepository;

    @Autowired
    public MixController(MixRepository mixRepository,
                         TrackRepository trackRepository,
                         MixConverter mixConverter,
                         AlbumRepository albumRepository) {
        this.mixRepository = mixRepository;
        this.trackRepository = trackRepository;
        this.mixConverter = mixConverter;
        this.albumRepository = albumRepository;
    }

    @GetMapping("/mixes")
    public Map<String, BigDecimal> getMixFor(@RequestParam(name = "staticTrackId") String staticTrackId,
                                             @RequestParam(name = "albumId") String albumId) {
        final var staticTrack = trackRepository.findById(staticTrackId)
                .orElseThrow(() -> new TrackNotFoundException(staticTrackId));
        return albumRepository.findById(AlbumId.from(albumId))
                .orElseThrow()
                .getTracks()
                .stream()
                .collect(
                        toMap(
                                Track::getId,
                                track -> MixPitchCalculator.calculatePitch(staticTrack, track)
                        )
                );
    }

    @GetMapping("/mix")
    public MixResponse getMix(@RequestParam(name = "staticTrackId") String staticTrackId,
                              @RequestParam(name = "dynamicTrackId") String dynamicTrackId) {
        final var staticTrack = trackRepository.findById(staticTrackId)
                .orElseThrow(() -> new TrackNotFoundException(staticTrackId));
        final var dynamicTrack = trackRepository.findById(dynamicTrackId)
                .orElseThrow(() -> new TrackNotFoundException(dynamicTrackId));

        return MixResponse.builder()
                .val(MixPitchCalculator.calculatePitch(staticTrack, dynamicTrack))
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MixResponse(BigDecimal val) {
    }

    @PostMapping("/mixes")
    public MixView addMix(@RequestBody @Validated MixCreateDto mixCreateDto) {
        final var mix = mixRepository.save(
                mixConverter.createMix(mixCreateDto)
        );
        return MixViewConverter.toMixView(mix);
    }
}

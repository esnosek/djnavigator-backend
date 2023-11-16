package dev.nos.djnavigator.collection.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.dto.MixCreateDto;
import dev.nos.djnavigator.collection.dto.MixView;
import dev.nos.djnavigator.collection.dto.converter.MixViewConverter;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.collection.repository.MixRepository;
import dev.nos.djnavigator.collection.repository.TrackRepository;
import dev.nos.djnavigator.collection.service.MixCreator;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static dev.nos.djnavigator.collection.service.MixPitchCalculator.calculatePitch;

@RestController
@RequestMapping("/api")
public class MixController {

    private final MixRepository mixRepository;
    private final TrackRepository trackRepository;
    private final MixCreator mixCreator;

    @Autowired
    public MixController(MixRepository mixRepository,
                         TrackRepository trackRepository,
                         MixCreator mixCreator) {
        this.mixRepository = mixRepository;
        this.trackRepository = trackRepository;
        this.mixCreator = mixCreator;
    }

    @GetMapping("/mix")
    public MixResponse getMix(@RequestParam(name = "staticTrackId") TrackId staticTrackId,
                              @RequestParam(name = "dynamicTrackId") TrackId dynamicTrackId) {
        final var staticTrack = trackRepository.findById(staticTrackId)
                .orElseThrow(() -> new TrackNotFoundException(staticTrackId));
        final var dynamicTrack = trackRepository.findById(dynamicTrackId)
                .orElseThrow(() -> new TrackNotFoundException(dynamicTrackId));

        return MixResponse.builder()
                .val(calculatePitch(staticTrack, dynamicTrack))
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MixResponse(BigDecimal val) {
    }

    @PostMapping("/mixes")
    public MixView addMix(@RequestBody @Validated MixCreateDto mixCreateDto) {
        final var mix = mixRepository.save(
                mixCreator.createMix(mixCreateDto)
        );
        return MixViewConverter.mixView(mix);
    }
}

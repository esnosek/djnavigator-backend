package dev.nos.djnavigator.collection.dto;

import dev.nos.djnavigator.collection.model.id.TrackId;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Optional;

@Builder
public record MixCreateDto(
        TrackId leftTrackId,
        Optional<BigDecimal> leftTrackPitch,
        TrackId rightTrackId
) {
}

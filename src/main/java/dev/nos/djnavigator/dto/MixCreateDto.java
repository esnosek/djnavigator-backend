package dev.nos.djnavigator.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Optional;

@Builder
public record MixCreateDto(
        String leftTrackId,
        Optional<BigDecimal> leftTrackPitch,
        String rightTrackId
) {
}

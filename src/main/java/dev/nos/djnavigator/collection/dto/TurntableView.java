package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TurntableView(
        String trackId,
        String trackName,
        BigDecimal pitch
) {
}

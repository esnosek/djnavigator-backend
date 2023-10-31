package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.nos.djnavigator.collection.model.id.TrackId;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TurntableView(
        TrackId trackId,
        String trackName,
        BigDecimal pitch
) {
}

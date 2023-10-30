package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MixView(
        String id,
        LocalDateTime createdDate,
        TurntableView leftTurntable,
        TurntableView rightTurntable) {
}

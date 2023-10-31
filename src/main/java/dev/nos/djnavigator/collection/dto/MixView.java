package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.nos.djnavigator.collection.model.id.MixId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MixView(
        @JsonUnwrapped
        MixId id,
        LocalDateTime createdDate,
        TurntableView leftTurntable,
        TurntableView rightTurntable) {
}

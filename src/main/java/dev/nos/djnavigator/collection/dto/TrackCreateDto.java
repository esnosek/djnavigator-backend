package dev.nos.djnavigator.collection.dto;

import dev.nos.djnavigator.collection.model.id.AlbumId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Builder
public record TrackCreateDto(
        @NotEmpty(message = "name has to be present")
        String name,
        @NotEmpty(message = "artists has to be present")
        List<@NotEmpty(message = "artist name has to be present") String> artists,
        AlbumId albumId,
        Optional<BigDecimal> tempo
) {
}

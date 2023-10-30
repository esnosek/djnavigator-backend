package dev.nos.djnavigator.collection.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record AlbumCreateDto(
        @NotEmpty(message = "name has to be present")
        String name,
        @NotEmpty(message = "artists has to be present")
        List<@NotEmpty(message = "artist name has to be present") String> artists
) {
}


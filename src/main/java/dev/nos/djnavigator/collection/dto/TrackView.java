package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TrackView(
        TrackId id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdDate,
        String name,
        List<String> artists,
        BigDecimal tempo,
        TrackSpotifyId spotifyId,
        AlbumView album
) {
    @JsonProperty("id")
    public String getId() {
        return id.id();
    }

    @JsonProperty("spotifyId")
    public String getSpotifyAlbumId() {
        return spotifyId != null ? spotifyId.id() : null;
    }
}

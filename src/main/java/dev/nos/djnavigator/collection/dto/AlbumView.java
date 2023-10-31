package dev.nos.djnavigator.collection.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlbumView(

        AlbumId id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdDate,
        String name,
        List<String> artists,
        List<TrackView> tracks,
        AlbumSpotifyId spotifyId,
        String imagePath
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

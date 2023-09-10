package dev.nos.djnavigator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlbumView(
        String id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdDate,
        String name,
        List<String> artists,
        List<TrackView> tracks,
        String spotifyId,
        String imagePath
) {

    public AlbumView withTracks(List<TrackView> tracks) {
        return copy()
                .tracks(tracks)
                .build();
    }

    private AlbumView.AlbumViewBuilder copy() {
        return AlbumView.builder()
                .id(this.id)
                .createdDate(this.createdDate)
                .name(this.name)
                .artists(this.artists)
                .spotifyId(this.spotifyId)
                .tracks(this.tracks)
                .imagePath(this.imagePath);
    }
}

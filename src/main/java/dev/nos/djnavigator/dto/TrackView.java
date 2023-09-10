package dev.nos.djnavigator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TrackView(
        String id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdDate,
        String name,
        List<String> artists,
        BigDecimal tempo,
        String spotifyId,
        AlbumView album
) {
    public TrackView withAlbum(AlbumView album) {
        return copy()
                .album(album)
                .build();
    }

    private TrackViewBuilder copy() {
        return TrackView.builder()
                .id(this.id)
                .createdDate(this.createdDate)
                .name(this.name)
                .artists(this.artists)
                .tempo(this.tempo)
                .spotifyId(this.spotifyId)
                .album(this.album);
    }
}

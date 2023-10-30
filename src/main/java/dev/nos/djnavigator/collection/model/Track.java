package dev.nos.djnavigator.collection.model;


import dev.nos.djnavigator.collection.model.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    private String name;
    @Convert(converter = StringListConverter.class)
    private List<String> artists;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
    private BigDecimal tempo;
    @Column(name = "spotify_id")
    private String spotifyId;

    public Track withAlbum(Album album) {
        this.album = album;
        return this;
    }
}

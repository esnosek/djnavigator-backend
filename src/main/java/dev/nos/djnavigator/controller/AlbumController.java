package dev.nos.djnavigator.controller;

import dev.nos.djnavigator.controller.creator.model.AlbumCreator;
import dev.nos.djnavigator.controller.creator.model.AlbumWithTrackCreator;
import dev.nos.djnavigator.controller.creator.view.AlbumViewWithTracksCreator;
import dev.nos.djnavigator.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.dto.AlbumCreateDto;
import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.repository.AlbumRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final AlbumCreator albumCreator;
    private final AlbumViewWithTracksCreator albumViewWithTracksCreator;
    private final AlbumWithTrackCreator albumWithTrackCreator;

    @Autowired
    public AlbumController(AlbumRepository albumRepository,
                           AlbumCreator albumCreator,
                           AlbumViewWithTracksCreator albumViewWithTracksCreator,
                           AlbumWithTrackCreator albumWithTrackCreator) {
        this.albumRepository = albumRepository;
        this.albumCreator = albumCreator;
        this.albumViewWithTracksCreator = albumViewWithTracksCreator;
        this.albumWithTrackCreator = albumWithTrackCreator;
    }

    @PostMapping("/albums")
    public AlbumView addAlbum(@RequestBody @Validated AlbumCreateDto albumCreateDto) {
        final var album = albumRepository.save(
                albumCreator.createAlbum(albumCreateDto)
        );
        return albumViewWithTracksCreator.albumViewWithTracks(album);
    }

    @PostMapping("/spotify-albums")
    public AlbumView addSpotifyAlbum(@RequestParam(name = "id") String spotifyAlbumId) {
        final var album = albumRepository.save(
                albumWithTrackCreator.createAlbumWithTracks(spotifyAlbumId)
        );
        return albumViewWithTracksCreator.albumViewWithTracks(album);
    }

    @GetMapping("/albums/{albumId}")
    public AlbumView getAlbum(@PathVariable String albumId) {
        final var album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        return albumViewWithTracksCreator.albumViewWithTracks(album);
    }

    @GetMapping("/albums")
    public List<AlbumView> getAlbums() {
        return stream(albumRepository.findAll().spliterator(), false)
                .map(albumViewWithTracksCreator::albumViewWithTracks)
                .toList();
    }

    @GetMapping("/albums/{albumId}/tracks")
    public List<TrackTempo> getAlbumWithTracks(@PathVariable String albumId) {
        final var album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        return album.getTracks().stream()
                .map(track -> TrackTempo.builder()
                        .trackId(track.getId())
                        .trackName(track.getName())
                        .tempo(track.getTempo())
                        .build()
                )
                .toList();
    }

    @Builder
    public record TrackTempo(
            String trackId,
            String trackName,
            BigDecimal tempo
    ) {

    }
}

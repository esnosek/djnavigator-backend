package dev.nos.djnavigator.controller;

import dev.nos.djnavigator.controller.converters.AlbumCreator;
import dev.nos.djnavigator.controller.converters.AlbumWithTrackCreator;
import dev.nos.djnavigator.controller.converters.TrackCreator;
import dev.nos.djnavigator.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.dto.AlbumCreateDto;
import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.repository.AlbumRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final AlbumCreator albumCreator;
    private final TrackCreator trackCreator;
    private final AlbumWithTrackCreator albumWithTrackCreator;

    @Autowired
    public AlbumController(AlbumRepository albumRepository,
                           AlbumCreator albumCreator,
                           TrackCreator trackCreator,
                           AlbumWithTrackCreator albumWithTrackCreator) {
        this.albumRepository = albumRepository;
        this.albumCreator = albumCreator;
        this.trackCreator = trackCreator;
        this.albumWithTrackCreator = albumWithTrackCreator;
    }

    @PostMapping("/albums")
    public AlbumView addAlbum(@RequestBody @Validated AlbumCreateDto albumCreateDto) {
        final var album = albumRepository.save(
                albumCreator.createAlbum(albumCreateDto)
        );
        return albumCreator.toAlbumView(album)
                .withTracks(trackViewsFrom(album));
    }

    @PostMapping("/spotify-albums")
    public AlbumView addSpotifyAlbum(@RequestParam(name = "id") String spotifyAlbumId) {
        final var album = albumRepository.save(
                albumWithTrackCreator.createAlbumWithTracks(spotifyAlbumId)
        );
        return albumCreator.toAlbumView(album)
                .withTracks(trackViewsFrom(album));
    }

    public static List<Integer> rotLeft(List<Integer> a, int d) {
        final var list = a.subList(0, d-1);
        final var list2 = a.subList(d, a.size() -1);
        list2.addAll(list);
        return list2;
    }

    @GetMapping("/albums/{albumId}")
    public AlbumView getAlbum(@PathVariable String albumId) {
        final var album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        return albumCreator.toAlbumView(album)
                .withTracks(trackViewsFrom(album));
    }

    @GetMapping("/albums")
    public List<AlbumView> getAlbums() {
        return stream(albumRepository.findAll().spliterator(), false)
                .map(album -> albumCreator
                        .toAlbumView(album)
                        .withTracks(trackViewsFrom(album))
                )
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

    private List<TrackView> trackViewsFrom(Album album) {
        return ofNullable(album.getTracks())
                .map(Collection::stream)
                .map(tracks -> tracks.map(trackCreator::toTrackView).toList())
                .orElse(emptyList());
    }

    @Builder
    public record TrackTempo(
            String trackId,
            String trackName,
            BigDecimal tempo
    ) {

    }
}

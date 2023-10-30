package dev.nos.djnavigator.collection.controller;

import dev.nos.djnavigator.collection.controller.converter.model.AlbumWithTrackCreator;
import dev.nos.djnavigator.collection.controller.converter.model.TrackConverter;
import dev.nos.djnavigator.collection.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.AlbumId;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.repository.AlbumRepository;
import dev.nos.djnavigator.collection.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static dev.nos.djnavigator.collection.controller.converter.view.TrackViewConverter.toTrackView;
import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api")
public class TrackController {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final TrackConverter trackConverter;
    private final AlbumWithTrackCreator albumWithTrackCreator;

    @Autowired
    public TrackController(TrackRepository trackRepository,
                           AlbumRepository albumRepository,
                           TrackConverter trackConverter,
                           AlbumWithTrackCreator albumWithTrackCreator) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.trackConverter = trackConverter;
        this.albumWithTrackCreator = albumWithTrackCreator;
    }

    @PostMapping("/tracks")
    public TrackView addTrack(@RequestBody @Validated TrackCreateDto trackCreateDto) {
        final var album = findAlbumOrThrow(trackCreateDto.albumId());
        final var track = trackRepository.save(
                trackConverter.createTrack(trackCreateDto, album)
        );
        return toTrackView(track, true);
    }

    @PostMapping("/spotify-tracks")
    public TrackView addSpotifyTrack(@RequestParam(name = "id") String spotifyTrackId) {
        albumRepository.save(
                albumWithTrackCreator.createAlbumWithTrack(spotifyTrackId)
        );
        final var track = trackRepository.findBySpotifyId(spotifyTrackId);
        return toTrackView(track, true);
    }

    @GetMapping("/tracks/{trackId}")
    public TrackView getTrack(@PathVariable String trackId) {
        final var track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(trackId));
        return toTrackView(track, true);
    }

    @GetMapping("tracks")
    public List<TrackView> getTracks(@RequestParam(required = false) String albumId) {
        final var tracks = null == albumId
                ? allTracks()
                : albumTracks(albumId);
        return tracks
                .map(track -> toTrackView(track, true))
                .toList();
    }

    @DeleteMapping("/tracks/{trackId}")
    public void deleteTrack(@PathVariable String trackId) {
        trackRepository.deleteById(trackId);
    }

    private Stream<Track> albumTracks(String albumId) {
        final var album = findAlbumOrThrow(albumId);
        return trackRepository.findByAlbumId(album.getId())
                .stream();
    }

    private Stream<Track> allTracks() {
        return stream(trackRepository.findAll().spliterator(), false);
    }

    private Album findAlbumOrThrow(String albumId) {
        return albumRepository.findById(AlbumId.from(albumId))
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

}

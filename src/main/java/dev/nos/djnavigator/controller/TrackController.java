package dev.nos.djnavigator.controller;

import dev.nos.djnavigator.controller.converters.AlbumCreator;
import dev.nos.djnavigator.controller.converters.AlbumWithTrackCreator;
import dev.nos.djnavigator.controller.converters.TrackCreator;
import dev.nos.djnavigator.controller.converters.TrackWithAlbumCreator;
import dev.nos.djnavigator.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.dto.TrackCreateDto;
import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.repository.AlbumRepository;
import dev.nos.djnavigator.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api")
public class TrackController {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final TrackCreator trackCreator;
    private final AlbumCreator albumCreator;
    private final TrackWithAlbumCreator trackWithAlbumCreator;
    private final AlbumWithTrackCreator albumWithTrackCreator;

    @Autowired
    public TrackController(TrackRepository trackRepository,
                           AlbumRepository albumRepository,
                           TrackCreator trackCreator,
                           AlbumCreator albumCreator,
                           TrackWithAlbumCreator trackWithAlbumCreator,
                           AlbumWithTrackCreator albumWithTrackCreator) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.trackCreator = trackCreator;
        this.albumCreator = albumCreator;
        this.trackWithAlbumCreator = trackWithAlbumCreator;
        this.albumWithTrackCreator = albumWithTrackCreator;
    }

    @PostMapping("/tracks")
    public TrackView addTrack(@RequestBody @Validated TrackCreateDto trackCreateDto) {
        final var album = findAlbumOrThrow(trackCreateDto.albumId());
        final var track = trackRepository.save(
                trackWithAlbumCreator.createTrackWithAlbum(trackCreateDto)
        );
        return trackCreator.toTrackView(track)
                .withAlbum(albumCreator.toAlbumView(track.getAlbum()));
    }

    @PostMapping("/spotify-tracks")
    public TrackView addSpotifyTrack(@RequestParam(name = "id") String spotifyTrackId) {
        albumRepository.save(
                albumWithTrackCreator.createAlbumWithTrack(spotifyTrackId)
        );
        final var track = trackRepository.findBySpotifyId(spotifyTrackId);
        return trackCreator.toTrackView(track)
                .withAlbum(albumCreator.toAlbumView(track.getAlbum()));
    }

    @GetMapping("/tracks/{trackId}")
    public TrackView getTrack(@PathVariable String trackId) {
        final var track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(trackId));
        return trackCreator.toTrackView(track)
                .withAlbum(albumCreator.toAlbumView(track.getAlbum()));
    }

    @GetMapping("tracks")
    public List<TrackView> getTracks(@RequestParam(required = false) String albumId) {
        final var tracks = null == albumId
                ? allTracks()
                : albumTracks(albumId);
        return tracks
                .map(track -> trackCreator
                        .toTrackView(track)
                        .withAlbum(albumCreator.toAlbumView(track.getAlbum()))
                )
                .toList();
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
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

}
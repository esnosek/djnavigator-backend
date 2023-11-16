package dev.nos.djnavigator.collection.controller;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import dev.nos.djnavigator.collection.repository.TrackQueries;
import dev.nos.djnavigator.collection.repository.TrackService;
import dev.nos.djnavigator.collection.service.TrackFromSpotifyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nos.djnavigator.collection.dto.converter.TrackViewConverter.trackView;

@RestController
@RequestMapping("/api")
public class TrackController {

    private final TrackService trackService;
    private final TrackQueries trackQueries;
    private final TrackFromSpotifyCreator trackFromSpotifyCreator;

    @Autowired
    public TrackController(TrackService trackService,
                           TrackQueries trackQueries,
                           TrackFromSpotifyCreator TrackFromSpotifyCreator) {
        this.trackService = trackService;
        this.trackQueries = trackQueries;
        this.trackFromSpotifyCreator = TrackFromSpotifyCreator;
    }

    @PostMapping("/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackView addTrack(@RequestBody @Validated TrackCreateDto trackCreateDto) {
        final var track = trackService.save(trackCreateDto);
        return trackView(track, true);
    }

    @PostMapping("/spotify-tracks")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackView addSpotifyTrack(@RequestParam(name = "id") TrackSpotifyId trackSpotifyId) {
        final var track = trackFromSpotifyCreator.saveTrackWithAlbum(trackSpotifyId);
        return trackView(track, true);
    }

    @GetMapping("/tracks/{trackId}")
    public TrackView getTrack(@PathVariable TrackId trackId) {
        final var track = trackQueries.getById(trackId);
        return trackView(track, true);
    }

    @GetMapping("tracks")
    public List<TrackView> getTracks(@RequestParam(required = false) AlbumId albumId) {
        return trackQueries.findByAlbumId(albumId)
                .stream()
                .map(track -> trackView(track, true))
                .toList();
    }

    @DeleteMapping("/tracks/{trackId}")
    public void deleteTrack(@PathVariable TrackId trackId) {
        trackService.deleteById(trackId);
    }

}

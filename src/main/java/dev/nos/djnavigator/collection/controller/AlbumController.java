package dev.nos.djnavigator.collection.controller;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.dto.AlbumView;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import dev.nos.djnavigator.collection.repository.AlbumQueries;
import dev.nos.djnavigator.collection.repository.AlbumService;
import dev.nos.djnavigator.collection.service.AlbumFromSpotifyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nos.djnavigator.collection.dto.converter.AlbumViewConverter.albumView;

@RestController
@RequestMapping("/api")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumQueries albumQueries;
    private final AlbumFromSpotifyCreator albumFromSpotifyCreator;

    @Autowired
    public AlbumController(AlbumService albumService,
                           AlbumQueries albumQueries,
                           AlbumFromSpotifyCreator albumFromSpotifyCreator) {
        this.albumService = albumService;
        this.albumQueries = albumQueries;
        this.albumFromSpotifyCreator = albumFromSpotifyCreator;
    }

    @PostMapping("/albums")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumView addAlbum(@RequestBody @Validated AlbumCreateDto albumCreateDto) {
        final var album = albumService.save(albumCreateDto);
        return albumView(album, true);
    }

    @PostMapping("/spotify-albums")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumView addSpotifyAlbum(@RequestParam(name = "id") AlbumSpotifyId albumSpotifyId) {
        final var album = albumFromSpotifyCreator.saveAlbumWithTracks(albumSpotifyId);
        return albumView(album, true);
    }

    @GetMapping("/albums/{albumId}")
    public AlbumView getAlbum(@PathVariable AlbumId albumId) {
        final var album = albumQueries.getById(albumId);
        return albumView(album, true);
    }

    @GetMapping("/albums")
    public List<AlbumView> getAlbums() {
        return albumQueries.findAll()
                .stream()
                .map(album -> albumView(album, true))
                .toList();
    }

    @DeleteMapping("/albums/{albumId}")
    public void deleteAlbum(@PathVariable AlbumId albumId) {
        albumService.deleteById(albumId);
    }
}

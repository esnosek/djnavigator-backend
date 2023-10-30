package dev.nos.djnavigator.collection.controller;

import dev.nos.djnavigator.collection.controller.converter.model.AlbumConverter;
import dev.nos.djnavigator.collection.controller.converter.model.AlbumWithTrackCreator;
import dev.nos.djnavigator.collection.controller.converter.view.AlbumViewConverter;
import dev.nos.djnavigator.collection.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.dto.AlbumView;
import dev.nos.djnavigator.collection.model.AlbumId;
import dev.nos.djnavigator.collection.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final AlbumConverter albumConverter;
    private final AlbumWithTrackCreator albumWithTrackCreator;

    @Autowired
    public AlbumController(AlbumRepository albumRepository,
                           AlbumConverter albumConverter,
                           AlbumWithTrackCreator albumWithTrackCreator) {
        this.albumRepository = albumRepository;
        this.albumConverter = albumConverter;
        this.albumWithTrackCreator = albumWithTrackCreator;
    }

    @PostMapping("/albums")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumView addAlbum(@RequestBody @Validated AlbumCreateDto albumCreateDto) {
        final var album = albumRepository.save(
                albumConverter.toAlbum(albumCreateDto)
        );
        return AlbumViewConverter.toAlbumView(album, true);
    }

    @PostMapping("/spotify-albums")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumView addSpotifyAlbum(@RequestParam(name = "id") String spotifyAlbumId) {
        final var album = albumRepository.save(
                albumWithTrackCreator.createAlbumWithTracks(spotifyAlbumId)
        );
        return AlbumViewConverter.toAlbumView(album, true);
    }

    @GetMapping("/albums/{albumId}")
    public AlbumView getAlbum(@PathVariable String albumId) {
        final var album = albumRepository.findById(AlbumId.from(albumId))
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        return AlbumViewConverter.toAlbumView(album, true);
    }

    @GetMapping("/albums")
    public List<AlbumView> getAlbums() {
        return stream(albumRepository.findAll().spliterator(), false)
                .map(album -> AlbumViewConverter.toAlbumView(album, true))
                .toList();
    }

    @DeleteMapping("/albums/{albumId}")
    public void deleteAlbum(@PathVariable String albumId) {
        final var album = albumRepository.findById(AlbumId.from(albumId))
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
        albumRepository.deleteById(album.getId());
    }
}

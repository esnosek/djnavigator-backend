package dev.nos.djnavigator.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;

public class SpotifyAlbumNotFoundException extends ResponseStatusException {

    public SpotifyAlbumNotFoundException(String spotifyAlbumId) {
        super(
                HttpStatus.NOT_FOUND,
                format("Album with spotifyId %s cannot be found in your collection", spotifyAlbumId)
        );
    }
}
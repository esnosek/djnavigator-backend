package dev.nos.djnavigator.collection.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;

public class AlbumNotFoundException extends ResponseStatusException {

    public AlbumNotFoundException(String albumId) {
        super(
                HttpStatus.NOT_FOUND,
                format("Album with id %s cannot be found in your collection", albumId)
        );
    }
}

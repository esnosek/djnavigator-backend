package dev.nos.djnavigator.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;

public class TrackNotFoundException extends ResponseStatusException {

    public TrackNotFoundException(String trackId) {
        super(
                HttpStatus.NOT_FOUND,
                format("Track with id %s cannot be found in your collection", trackId)
        );
    }
}

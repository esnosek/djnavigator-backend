package dev.nos.djnavigator.spotify.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class SpotifyException extends ResponseStatusException {

    public SpotifyException(HttpStatusCode httpStatus, String message) {
        super(httpStatus, message);
    }
}

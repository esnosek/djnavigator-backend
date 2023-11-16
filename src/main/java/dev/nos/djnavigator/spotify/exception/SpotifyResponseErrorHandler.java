package dev.nos.djnavigator.spotify.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class SpotifyResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        var mapper = new ObjectMapper();
        var errorMessage = mapper.readTree(response.getBody())
                .get("error")
                .get("message")
                .asText();
        throw new SpotifyException(response.getStatusCode(), errorMessage);
    }
}

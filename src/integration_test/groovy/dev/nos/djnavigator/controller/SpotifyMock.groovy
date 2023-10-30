package dev.nos.djnavigator.controller

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer

import static dev.nos.djnavigator.controller.TestUtils.parsedJson
import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus

class SpotifyMock {

    private final MockRestServiceServer mockServer;

    SpotifyMock(MockRestServiceServer mockServer) {
        this.mockServer = mockServer;
    }

    def mockSpotifyAuthorization(ExpectedCount count,
                                 String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://accounts.spotify.com/api/token")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(header("Authorization", containsString("Basic ")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetSpotifyAlbum(ExpectedCount count,
                            String token,
                            String spotifyAlbumId,
                            String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/albums/" + spotifyAlbumId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer " + token)))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetAudioFeatures(ExpectedCount count,
                             String token,
                             List<String> spotifyTracksIds,
                             String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/audio-features?ids=" + String.join(",", spotifyTracksIds))))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer " + token)))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockNotFoundErrorOnGetSpotifyAlbum(ExpectedCount count,
                                           String token,
                                           String spotifyAlbumId,
                                           String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/albums/" + spotifyAlbumId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer " + token)))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }
}

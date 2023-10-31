package dev.nos.djnavigator.spotify

import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer

import static dev.nos.djnavigator.collection.controller.TestUtils.parsedJson
import static java.lang.String.format
import static java.net.URLEncoder.encode
import static java.nio.charset.StandardCharsets.UTF_8
import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.client.ExpectedCount.between
import static org.springframework.test.web.client.ExpectedCount.once
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus

class SpotifyMock {

    private final MockRestServiceServer mockServer;

    SpotifyMock(MockRestServiceServer mockServer) {
        this.mockServer = mockServer;
    }

    def mockSpotifyAuthorization() {
        def tokenValue = "12341234"
        def tokenResponse =
                """{
                    "access_token": "${tokenValue}",
                    "expires_in": 3600
                }"""
        mockServer
                .expect(between(0, 1), requestTo(URI.create("https://accounts.spotify.com/api/token")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(header("Authorization", containsString("Basic ")))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(tokenResponse))
                )
        return tokenValue
    }

    def mockSpotifySearch(String query, int limit, String response) {
        def requestURI = URI.create(
                format(
                        "https://api.spotify.com/v1/search?q=%s&type=%s&limit=%s",
                        encode(query, UTF_8),
                        "album,track",
                        limit
                )
        )
        mockServer
                .expect(once(), requestTo(requestURI))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetSpotifyAlbum(ExpectedCount count,
                            SpotifyAlbumId spotifyAlbumId,
                            String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/albums/" + spotifyAlbumId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetSpotifyTrack(ExpectedCount count,
                            SpotifyTrackId spotifyTrackId,
                            String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/tracks/" + spotifyTrackId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockNotFoundErrorOnGetSpotifyTrack(ExpectedCount count,
                                           SpotifyTrackId spotifyTrackId,
                                           String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/tracks/" + spotifyTrackId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetAudioFeatures(ExpectedCount count,
                             SpotifyTrackId spotifyTrackId,
                             String response) {

        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/audio-features/" + spotifyTrackId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetAudioFeatures(ExpectedCount count,
                             List<SpotifyTrackId> spotifyTracksIds,
                             String response) {

        def spotifyTracksIdsAsString = spotifyTracksIds
                .stream()
                .map(SpotifyTrackId::id)
                .toList()

        mockServer
                .expect(count, requestTo(URI.create(
                        "https://api.spotify.com/v1/audio-features?ids=" +
                                String.join(",", spotifyTracksIdsAsString)))
                )
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockNotFoundErrorOnGetSpotifyAlbum(ExpectedCount count,
                                           SpotifyAlbumId spotifyAlbumId,
                                           String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/albums/" + spotifyAlbumId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockGetSpotifyPlaylist(ExpectedCount count,
                               SpotifyPlaylistId spotifyPlaylistId,
                               String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/playlists/" + spotifyPlaylistId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }

    def mockNotFoundErrorOnGetSpotifyPlaylist(ExpectedCount count,
                                              SpotifyPlaylistId spotifyPlaylistId,
                                              String response) {
        mockServer
                .expect(count, requestTo(URI.create("https://api.spotify.com/v1/playlists/" + spotifyPlaylistId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", containsString("Bearer ")))
                .andExpect(header("Content-Type", "application/json"))
                .andRespond(
                        withStatus(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(parsedJson(response))
                )
    }
}

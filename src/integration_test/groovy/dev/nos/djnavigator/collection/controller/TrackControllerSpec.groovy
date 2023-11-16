package dev.nos.djnavigator.collection.controller

import com.fasterxml.jackson.databind.JsonNode
import dev.nos.djnavigator.collection.model.id.AlbumId
import dev.nos.djnavigator.collection.model.id.TrackId
import dev.nos.djnavigator.collection.repository.AlbumRepository
import dev.nos.djnavigator.collection.repository.TrackRepository
import dev.nos.djnavigator.config.TestConfig
import dev.nos.djnavigator.spotify.SpotifyMock
import dev.nos.djnavigator.utils.time.Clock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

import static dev.nos.djnavigator.TestData.*
import static dev.nos.djnavigator.collection.controller.TestUtils.*
import static java.lang.String.format
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsString
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.client.ExpectedCount.once

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ContextConfiguration
@ActiveProfiles("test")
class TrackControllerSpec extends Specification {
    @Autowired
    private WebTestClient client

    @Autowired
    private AlbumRepository albumRepository

    @Autowired
    private TrackRepository trackRepository

    @Autowired
    private RestTemplate restTemplate

    @Autowired
    private Clock clock

    private MockRestServiceServer mockServer
    private SpotifyMock spotifyMock

    void setup() {
        mockServer = MockRestServiceServer
                .bindTo(restTemplate)
                .ignoreExpectOrder(true)
                .build()
        spotifyMock = new SpotifyMock(mockServer)
        clock.setFixed(Instant.parse("2018-08-19T16:02:42.000Z"))
    }

    void cleanup() {
        albumRepository.deleteAll()
        trackRepository.deleteAll()
    }

    def "POST /api/tracks should save track and return track view"() {
        given:
        def album = album()
                .createdDate(clock.now())
                .artists(List.of("artist1", "artist2"))
                .build()
        albumRepository.save(album)

        def trackCreateDtoJson =
                """{   
                    "name": "first track",
                    "artists": ["artist1", "artist2"],
                    "tempo": 131.05,
                    "albumId": "${album.getId()}"
                }"""

        expect:
        def expectedResponse =
                """{
                    "id": "\${json-unit.matches:idMatcher}",
                    "createdDate": "\${json-unit.matches:dateMatcher}",
                    "name": "first track",
                    "artists": ["artist1", "artist2"],
                    "tempo": 131.05,
                    "album": {
                        "id": "${album.getId()}",
                        "createdDate": "2018-08-19T16:02:42.000Z",
                        "name": "${album.getName()}",
                        "artists": [
                            "artist1",
                            "artist2"
                        ],
                        "spotifyId": "${album.getSpotifyId()}",
                        "imagePath": "${album.getImagePath()}"
                    }
                }"""

        def trackJson = client.post()
                .uri("/api/tracks")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(trackCreateDtoJson))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
                .returnResult()
                .responseBody

        def trackId = TrackId.from(trackJson.get("id").asText())
        trackRepository.findById(trackId).isPresent()
    }

    def "POST /api/tracks should return 404 when album cannot be found in the collection"() {
        given:
        def album = album()
                .artists(List.of("artist1", "artist2"))
                .build()

        def trackCreateDtoJson =
                """{   
                    "name": "first track",
                    "artists": ["artist1", "artist2"],
                    "tempo": 131.05,
                    "albumId": "${album.getId()}"
                }"""

        expect:
        client.post()
                .uri("/api/tracks")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(trackCreateDtoJson))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString(format("Album with id %s cannot be found in your collection", album.getId()))
                    )
                }

    }

    def "POST /api/tracks should return 400 when the request body has unknown field"() {
        given:
        def trackCreateDtoJson =
                '''{   
                    "name": "first album",
                    "artists": ["artist1", "artist2"],
                    "tempo": 131.05,
                    "albumId": "1234",
                    "unknownField": "xxx"
                }'''

        expect:
        client.post()
                .uri("/api/tracks")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(trackCreateDtoJson))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("JSON parse error: Unrecognized field \"unknownField\"")
                    )
                }
    }

    def "POST /api/tracks should return 400 when the request body does not contain required field"() {
        given:
        def trackCreateDtoJson =
                '''{   
                    "artists": ["artist1", "artist2"]
                }'''

        expect:
        client.post()
                .uri("/api/tracks")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(trackCreateDtoJson))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("Validation failed for object='trackCreateDto'. Error count: 2")
                    )
                }
    }

    def "POST /api/spotify-tracks should save track with album and return track view"() {
        given:
        def spotifyTrack = spotifyTrack().build()
        def spotifyAlbum = spotifyAlbum()
                .spotifyTracks(List.of(spotifyTrack))
                .build()

        spotifyMock.mockSpotifyAuthorization()

        def spotifyTrackResponse =
                """{
                    "id": "${spotifyTrack.spotifyId()}",
                    "name": "Embody Control",
                    "artists": [
                        {"name" : "Normal Bias 1"},
                        {"name" : "Normal Bias 2"}
                    ],
                    "album": {
                        "id": "${spotifyAlbum.spotifyId()}",
                        "name": "Normal Bias album",
                        "artists": [
                            {"name" : "Normal Bias 3"}
                        ],
                        "images" : [
                            {"url": "url1"},
                            {"url": "url2"}
                        ]
                    }
                }"""
        spotifyMock.mockGetSpotifyTrack(
                once(),
                spotifyTrack.spotifyId(),
                spotifyTrackResponse
        )

        def audioFeaturesResponse =
                """{   
                    "id":  "${spotifyTrack.spotifyId()}",
                    "tempo": 131.054
                }"""
        spotifyMock.mockGetAudioFeatures(
                once(),
                spotifyTrack.spotifyId(),
                audioFeaturesResponse
        )

        def spotifyAlbumResponse =
                """{
                    "id": "${spotifyAlbum.spotifyId()}",
                    "name": "first spotify album",
                    "artists": [
                        {"name": "artist1"},
                        {"name": "artist2"}
                    ],
                    "images": [
                        {"url": "imagePath1"},
                        {"url": "imagePath2"}
                    ],
                    "tracks": {
                        "items" : [
                            {
                                "id": "${spotifyTrack.spotifyId()}",
                                "name": "track1",
                                "artists": [
                                    {"name": "artist1"},
                                    {"name": "artist2"}
                                ]
                            },
                            {
                                "id": "someId",
                                "name": "track2",
                                "artists": [
                                    {"name": "artist1"},
                                    {"name": "artist2"}
                                ]
                            }
                        ]
                    }
                }"""
        spotifyMock.mockGetSpotifyAlbum(
                once(),
                spotifyAlbum.spotifyId(),
                spotifyAlbumResponse
        )

        expect:
        def expectedResponse =
                """{
                    "id": "\${json-unit.matches:idMatcher}",
                    "createdDate": "\${json-unit.matches:dateMatcher}",
                    "name": "Embody Control",
                    "artists": [
                        "Normal Bias 1",
                        "Normal Bias 2"
                    ],
                    "tempo": 131.05,
                    "spotifyId": "${spotifyTrack.spotifyId()}",
                    "album": {
                        "id": "\${json-unit.matches:idMatcher}",
                        "createdDate": "\${json-unit.matches:dateMatcher}",
                        "name": "first spotify album",
                        "artists": [
                            "artist1",
                            "artist2"
                        ],
                        "spotifyId": "${spotifyAlbum.spotifyId()}",
                        "imagePath": "imagePath1"
                    }
                }"""

        def trackJson = client.post()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/spotify-tracks")
                            .queryParam("id", spotifyTrack.spotifyId())
                            .build()
                }
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
                .returnResult()
                .responseBody

        and: "track is saved"
        def trackId = TrackId.from(trackJson.get("id").asText())
        trackRepository.findById(trackId).isPresent()

        and: "album is saved"
        def albumId = AlbumId.from(trackJson.get("album").get("id").asText())
        albumRepository.findById(albumId).isPresent()
    }

    @Unroll
    def "POST /api/spotify-tracks should return #statusCode when spotify api return #statusCode"() {
        given:
        def spotifyTrackId = spotifyTrack().build().spotifyId()

        spotifyMock.mockSpotifyAuthorization()

        def errorResponse =
                """{
                    "error": {
                        "message" : "some message"
                    }
                }"""
        spotifyMock.mockErrorOnGetSpotifyTrack(
                once(),
                spotifyTrackId,
                errorResponse,
                statusCode
        )

        expect:
        client.post()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/spotify-tracks")
                            .queryParam("id", spotifyTrackId)
                            .build()
                }
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(statusCode)
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("some message")
                    )
                }

        where:
        statusCode << [NOT_FOUND, TOO_MANY_REQUESTS]
    }

    def "GET /api/tracks/{id} should return track view"() {
        given:
        def album = albumRepository.save(album().build())
        def track = trackRepository.save(track().album(album).build())

        def expectedResponse =
                """{   
                    "id": "${track.getId()}",
                    "createdDate": "${formatDate(track.getCreatedDate())}",
                    "name": "${track.getName()}",
                    "artists":["artist1","artist2"],
                    "tempo": 123.12,
                    "spotifyId": "${track.getSpotifyId()}",
                    "album": {
                        "id": "${album.getId()}",
                        "createdDate": "${formatDate(album.getCreatedDate())}",
                        "name": "${album.getName()}",
                        "artists": [ "artist1", "artist2"],
                        "spotifyId": "${album.getSpotifyId()}",
                        "imagePath": "${album.getImagePath()}"
                    }
                }"""

        expect:
        client.get()
                .uri(format("/api/tracks/%s", track.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET /api/tracks/{id} should return 404 when track cannot be found in the collection"() {
        expect:
        client.get()
                .uri("/api/tracks/1234")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Track with id 1234 cannot be found in your collection")
    }

    def "GET /api/tracks?albumId={id} should return all tracks from album"() {
        given:
        def album = albumRepository.save(album().build())
        def track1 = trackRepository.save(track().album(album).build())
        def track2 = trackRepository.save(track().album(album).build())

        def expectedResponse =
                """[
                    {   
                        "id": "${track1.getId()}",
                        "createdDate": "${formatDate(track1.getCreatedDate())}",
                        "name": "${track1.getName()}",
                        "artists":["artist1","artist2"],
                        "tempo": 123.12,
                        "spotifyId": "${track1.getSpotifyId()}",
                        "album": {
                            "id": "${album.getId()}",
                            "createdDate": "${formatDate(album.getCreatedDate())}",
                            "name": "${album.getName()}",
                            "artists": [ "artist1", "artist2"],
                            "spotifyId": "${album.getSpotifyId()}",
                            "imagePath": "${album.getImagePath()}"
                        }
                    },
                    {   
                        "id": "${track2.getId()}",
                        "createdDate": "${formatDate(track2.getCreatedDate())}",
                        "name": "${track2.getName()}",
                        "artists":["artist1","artist2"],
                        "tempo": 123.12,
                        "spotifyId": "${track2.getSpotifyId()}",
                        "album": {
                            "id": "${album.getId()}",
                            "createdDate": "${formatDate(album.getCreatedDate())}",
                            "name": "${album.getName()}",
                            "artists": [ "artist1", "artist2"],
                            "spotifyId": "${album.getSpotifyId()}",
                            "imagePath": "${album.getImagePath()}"
                        }
                    }
                ]"""

        expect:
        client.get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/tracks")
                            .queryParam("albumId", album.getId())
                            .build()
                }
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET /api/tracks?albumId={id} should return 404 when album cannot be found in the collection"() {
        expect:
        client.get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/tracks")
                            .queryParam("albumId", "1234")
                            .build()
                }
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Album with id 1234 cannot be found in your collection")
    }

    def "DELETE /api/tracks/{id} should delete track from the collection"() {
        given:
        def track = trackRepository.save(track().build())

        expect:
        client.delete()
                .uri(format("/api/tracks/%s", track.getId()))
                .exchange()
                .expectStatus()
                .isOk()

        and: "album has been deleted"
        trackRepository.findById(track.id).isEmpty()
    }

    def "DELETE /api/tracks/{id} should return 404 when track cannot be found in the collection"() {
        expect:
        client.delete()
                .uri("/api/tracks/1234")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Track with id 1234 cannot be found in your collection")
    }
}

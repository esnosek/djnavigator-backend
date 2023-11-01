package dev.nos.djnavigator.collection.controller

import com.fasterxml.jackson.databind.JsonNode
import dev.nos.djnavigator.collection.model.id.AlbumId
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId
import dev.nos.djnavigator.collection.model.id.TrackId
import dev.nos.djnavigator.collection.repository.AlbumRepository
import dev.nos.djnavigator.collection.repository.TrackRepository
import dev.nos.djnavigator.config.TestConfig
import dev.nos.djnavigator.spotify.SpotifyMock
import dev.nos.djnavigator.time.Clock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.time.Instant

import static dev.nos.djnavigator.TestData.*
import static dev.nos.djnavigator.collection.controller.TestUtils.*
import static java.lang.String.format
import static java.util.stream.StreamSupport.stream
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsString
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.client.ExpectedCount.once
import static org.springframework.test.web.client.ExpectedCount.twice

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfig.class
)
@ContextConfiguration
@ActiveProfiles("test")
class AlbumControllerSpec extends Specification {

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
        clock.setFixed(Instant.parse("2018-08-19T16:02:42.00Z"))
    }

    void cleanup() {
        albumRepository.deleteAll()
        trackRepository.deleteAll()
    }

    def "POST /api/albums should save album and return album view"() {
        given:
        def albumCreateDtoJson =
                '''{   
                    "name": "first album",
                    "artists": ["artist1", "artist2"]
                }'''

        expect:
        def expectedResponse =
                '''{
                    "id": "${json-unit.matches:idMatcher}",
                    "createdDate": "${json-unit.matches:dateMatcher}",
                    "name": "first album",
                    "artists": ["artist1", "artist2"],
                    "tracks": []
                }'''

        def albumJson = client.post()
                .uri("/api/albums")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(albumCreateDtoJson))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
                .returnResult()
                .responseBody

        def albumId = AlbumId.from(albumJson.get("id").asText())
        albumRepository.findById(albumId).isPresent()
    }

    def "POST /api/albums should return 400 http code when the request body has unknown field"() {
        given:
        def albumCreateDtoJson =
                '''{   
                    "name": "first album",
                    "artists": ["artist1", "artist2"],
                    "unknownField": "xxx"
                }'''

        expect:
        client.post()
                .uri("/api/albums")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(albumCreateDtoJson))
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

    def "POST /api/albums should return 400 http code when the request body does not contain required field"() {
        given:
        def albumCreateDtoJson =
                '''{   
                    "artists": ["artist1", "artist2"]
                }'''

        expect:
        client.post()
                .uri("/api/albums")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(parsedJson(albumCreateDtoJson))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("Validation failed for object='albumCreateDto'. Error count: 1")
                    )
                }
    }

    def "POST /api/spotify-albums should save album with all tracks and return album view"() {
        given:
        def spotifyTrack1 = spotifyTrack().build()
        def spotifyTrack2 = spotifyTrack().build()
        def spotifyAlbum = spotifyAlbum()
                .spotifyTracks(List.of(spotifyTrack1, spotifyTrack2))
                .build()

        spotifyMock.mockSpotifyAuthorization()

        def spotifyAlbumResponse =
                """{
                    "id": "997",
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
                                "id": "${spotifyTrack1.spotifyId()}",
                                "name": "track1",
                                "artists": [
                                    {"name": "artist1"},
                                    {"name": "artist2"}
                                ]
                            },
                            {
                                "id": "${spotifyTrack2.spotifyId()}",
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
                twice(),
                spotifyAlbum.spotifyId(),
                spotifyAlbumResponse
        )

        def audioFeaturesResponse =
                """{   
                    "audio_features": [
                    {
                        "id":  "${spotifyTrack1.spotifyId()}",
                        "tempo": 131.054
                    },
                    {
                        "id": "${spotifyTrack2.spotifyId()}",
                        "tempo": 91.345
                    }
                    ]
                }"""
        spotifyMock.mockGetAudioFeatures(
                once(),
                List.of(spotifyTrack1.spotifyId(), spotifyTrack2.spotifyId()),
                audioFeaturesResponse
        )

        expect:
        def expectedResponse =
                """{
                    "id": "\${json-unit.matches:idMatcher}",
                    "createdDate": "\${json-unit.matches:dateMatcher}",
                    "name": "first spotify album",
                    "artists": ["artist1", "artist2"],
                    "tracks": [
                        {
                            "id": "\${json-unit.matches:idMatcher}",
                            "createdDate": "\${json-unit.matches:dateMatcher}",
                            "name": "track1",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "tempo": 131.05,
                            "spotifyId": "${spotifyTrack1.spotifyId()}"
                        },
                        {
                            "id": "\${json-unit.matches:idMatcher}",
                            "createdDate": "\${json-unit.matches:dateMatcher}",
                            "name": "track2",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "tempo": 91.35,
                            "spotifyId": "${spotifyTrack2.spotifyId()}"
                        }
                    ],
                    "spotifyId":"997",
                    "imagePath":"imagePath1"
                }"""

        def albumJson = client.post()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/spotify-albums")
                            .queryParam("id", spotifyAlbum.spotifyId())
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

        and: "album is saved"
        def albumId = AlbumId.from(albumJson.get("id").asText())
        albumRepository.findById(albumId).isPresent()

        and: "all album tracks are saved"
        stream(albumJson.get("tracks").spliterator(), false)
                .map { it.get("id").asText() }
                .forEach { trackRepository.findById(TrackId.from(it)).isPresent() }
    }

    def "POST /api/spotify-albums should return 404 when spotify album cannot be found"() {
        given:
        def spotifyAlbumId = spotifyAlbum().build().spotifyId()

        spotifyMock.mockSpotifyAuthorization()

        def errorResponse =
                """{
                    "error": {
                        "message" : "Non existing id: 'spotify:album:${spotifyAlbumId}'"
                    }
                }"""
        spotifyMock.mockNotFoundErrorOnGetSpotifyAlbum(
                once(),
                spotifyAlbumId,
                errorResponse
        )

        expect:
        client.post()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/spotify-albums")
                            .queryParam("id", spotifyAlbumId)
                            .build()
                }
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("Non existing id: 'spotify:album:${spotifyAlbumId}'")
                    )
                }
    }

    def "GET /api/albums/{id} should return album"() {
        given:
        def album = albumRepository.save(album().build())

        def expectedResponse =
                """{   
                    "id":"${album.getId()}",
                    "createdDate":"${formatDate(album.getCreatedDate())}",
                    "name":"album name",
                    "artists":["artist1","artist2"],
                    "tracks":[],
                    "spotifyId":"spotifyId"
                }"""

        expect:
        client.get()
                .uri(format("/api/albums/%s", album.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(parsedJson(expectedResponse))
    }

    def "should GET /api/albums/{id} return 404 http code when album cannot be found in the collection"() {
        expect:
        client.get()
                .uri("/api/albums/1234")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Album with id 1234 cannot be found in your collection")
    }

    def "GET /api/albums should return all albums from the collection"() {
        given:
        def album1 = albumRepository.save(
                album()
                        .spotifyId(AlbumSpotifyId.from("spotifyId1"))
                        .build()
        )
        def album2 = albumRepository.save(
                album()
                        .spotifyId(AlbumSpotifyId.from("spotifyId2"))
                        .build()
        )

        expect:
        def expectedResponse =
                """[
                    {   
                        "id":"${album1.getId()}",
                        "createdDate":"${formatDate(album1.getCreatedDate())}",
                        "name":"album name",
                        "artists":["artist1","artist2"],
                        "tracks":[],
                        "spotifyId":"spotifyId1"
                    },
                    {   
                        "id":"${album2.getId()}",
                        "createdDate":"${formatDate(album2.getCreatedDate())}",
                        "name":"album name",
                        "artists":["artist1","artist2"],
                        "tracks":[],
                        "spotifyId":"spotifyId2"
                    }
                ]"""

        client.get()
                .uri("/api/albums")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(parsedJson(expectedResponse))
    }

    def "DELETE /api/albums/{id} should delete album from collection"() {
        given:
        def album = albumRepository.save(album().build())

        expect:
        client.delete()
                .uri(format("/api/albums/%s", album.getId()))
                .exchange()
                .expectStatus()
                .isOk()

        and: "album has been deleted"
        albumRepository.findById(album.id).isEmpty()
    }

    def "DELETE /api/albums/{id} should return 404 http code when album cannot be found in the collection"() {
        expect:
        client.delete()
                .uri("/api/albums/1234")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Album with id 1234 cannot be found in your collection")
    }
}
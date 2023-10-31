package dev.nos.djnavigator.spotify

import com.fasterxml.jackson.databind.JsonNode
import dev.nos.djnavigator.collection.repository.AlbumRepository
import dev.nos.djnavigator.collection.repository.TrackRepository
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static dev.nos.djnavigator.TestData.spotifyAlbum
import static dev.nos.djnavigator.collection.controller.TestUtils.assertJson
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsString
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.client.ExpectedCount.once
import static org.springframework.test.web.client.ExpectedCount.twice

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ActiveProfiles("test")
class SpotifyControllerSpec extends Specification {

    @Autowired
    private WebTestClient client

    @Autowired
    private AlbumRepository albumRepository

    @Autowired
    private TrackRepository trackRepository

    @Autowired
    private RestTemplate restTemplate

    private MockRestServiceServer mockServer
    private SpotifyMock spotifyMock

    void setup() {
        mockServer = MockRestServiceServer
                .bindTo(restTemplate)
                .ignoreExpectOrder(true)
                .build()
        spotifyMock = new SpotifyMock(mockServer)
    }

    void cleanup() {
        albumRepository.deleteAll()
        trackRepository.deleteAll()
    }

    def "GET spotify/search should return search result"() {
        given:
        def spotifyTrackId1 = SpotifyTrackId.randomId()
        def spotifyTrackId2 = SpotifyTrackId.randomId()
        def spotifyAlbumId1 = SpotifyAlbumId.randomId()
        def spotifyAlbumId2 = SpotifyAlbumId.randomId()
        def query = "AlphaOmega"
        def limit = 5

        spotifyMock.mockSpotifyAuthorization()

        def searchResponse =
                """{
                    "tracks": {
                        "items": [
                            {
                                "id": "${spotifyTrackId1}",
                                "name": "Embody Control",
                                "artists": [
                                    {"name" : "Normal Bias 1"},
                                    {"name" : "Normal Bias 2"}
                                ],
                                "album": {
                                    "id": "${spotifyAlbumId1}",
                                    "name": "Normal Bias album",
                                    "artists": [
                                        {"name" : "Normal Bias 3"}
                                    ],
                                    "images" : [
                                        {"url": "url1"},
                                        {"url": "url2"}
                                    ]
                                }
                            },
                            {
                                "id": "${spotifyTrackId2}",
                                "name": "Guru Sru",
                                "artists": [
                                    {"name" : "Normal Bias 4"},
                                    {"name" : "Normal Bias 5"}
                                ],
                                "album": {
                                    "id": "${spotifyAlbumId2}",
                                    "name": "Normal Bias album",
                                    "artists": [
                                        {"name" : "Normal Bias 6"}
                                    ],
                                    "images" : [
                                        {"url": "url3"},
                                        {"url": "url4"}
                                    ]
                                }
                            }
                        ]
                    },
                    "albums": {
                        "items" : [
                            {
                                "id": "${spotifyAlbumId1}",
                                "name": "Normal Bias",
                                "artists": [
                                    {"name" : "Normal Bias 7"},
                                    {"name" : "Normal Bias 8"}
                                ],
                                "images" : [
                                    {"url": "url5"},
                                    {"url": "url6"}
                                ]
                            },
                            {
                                "id": "${spotifyAlbumId2}",
                                "name": "Mouth (Normal Bias Remix)",
                                "artists": [
                                    {"name" : "Normal Bias 9"}
                                ],
                                "images" : [
                                    {"url": "url7"},
                                    {"url": "url8"}
                                ]
                            }
                        ]   
                    }
                }"""

        spotifyMock.mockSpotifySearch(query, limit, searchResponse)

        expect:
        def expectedResponse = """{
        "tracks": [
            {
                "spotifyId": "${spotifyTrackId1}",
                "name": "Embody Control",
                "artists": [
                    "Normal Bias 1",
                    "Normal Bias 2"
                ],
                "spotifyAlbum": {
                    "spotifyId": "${spotifyAlbumId1}",
                    "name": "Normal Bias album",
                    "artists": [
                        "Normal Bias 3"
                    ],
                    "imagePath": "url1"
                },
                "imagePath": "url1"
            },
            {
                "spotifyId": "${spotifyTrackId2}",
                "name": "Guru Sru",
                "artists": [
                    "Normal Bias 4",
                    "Normal Bias 5"
                ],
                "spotifyAlbum": {
                    "spotifyId": "${spotifyAlbumId2}",
                    "name": "Normal Bias album",
                    "artists": [
                        "Normal Bias 6"
                    ],
                    "imagePath": "url3"
                },
                "imagePath": "url3"
            }
            ],
            "albums": [
                {
                    "spotifyId": "${spotifyAlbumId1}",
                    "name": "Normal Bias",
                    "artists": [
                        "Normal Bias 7",
                        "Normal Bias 8"
                    ],
                    "imagePath": "url5"
                },
                {
                    "spotifyId": "${spotifyAlbumId2}",
                    "name": "Mouth (Normal Bias Remix)",
                    "artists": [
                        "Normal Bias 9"
                    ],
                    "imagePath": "url7"
                }
            ]
        }"""

        client.get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path("/api/spotify/search")
                            .queryParam("query", query)
                            .queryParam("limit", limit)
                            .build()
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET spotify/tracks/{id} should return spotify track with audio features"() {
        given:
        def spotifyTrackId = SpotifyTrackId.randomId()
        def spotifyAlbumId = SpotifyAlbumId.randomId()

        spotifyMock.mockSpotifyAuthorization()

        def spotifyTrackResponse =
                """{
                    "id": "${spotifyTrackId}",
                    "name": "Embody Control",
                    "artists": [
                        {"name" : "Normal Bias 1"},
                        {"name" : "Normal Bias 2"}
                    ],
                    "album": {
                        "id": "${spotifyAlbumId}",
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
        spotifyMock.mockGetSpotifyTrack(once(), spotifyTrackId, spotifyTrackResponse)

        def audioFeaturesResponse =
                """{   
                    "id":  "${spotifyTrackId}",
                    "tempo": 131.054
                }"""
        spotifyMock.mockGetAudioFeatures(
                once(),
                spotifyTrackId,
                audioFeaturesResponse
        )

        expect:
        def expectedResponse =
                """{
                    "spotifyId": "${spotifyTrackId}",
                    "name": "Embody Control",
                    "artists": [
                        "Normal Bias 1",
                        "Normal Bias 2"
                    ],
                    "audioFeatures": {
                        "id": "${spotifyTrackId}",
                        "tempo": 131.05
                    },
                    "spotifyAlbum": {
                        "spotifyId": "${spotifyAlbumId}",
                        "name": "Normal Bias album",
                        "artists": [
                            "Normal Bias 3"
                        ],
                        "imagePath": "url1"
                    },
                    "imagePath": "url1"
                }"""

        client.get()
                .uri("/api/spotify/tracks/" + spotifyTrackId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET spotify/tracks/{id} should return 404 when spotify track cannot be found"() {
        given:
        def spotifyTrackId = SpotifyTrackId.randomId()

        spotifyMock.mockSpotifyAuthorization()

        def errorResponse =
                """{
                    "error": {
                        "message" : "Non existing id: 'spotify:track:${spotifyTrackId}'"
                    }
                }"""
        spotifyMock.mockNotFoundErrorOnGetSpotifyTrack(
                once(),
                spotifyTrackId,
                errorResponse
        )

        expect:
        client.get()
                .uri("/api/spotify/tracks/" + spotifyTrackId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("Non existing id: 'spotify:track:${spotifyTrackId}'")
                    )
                }
    }

    def "GET spotify/albums/{id} should return spotify album with tracks and audio features"() {
        given:
        def spotifyTrackId1 = SpotifyTrackId.randomId()
        def spotifyTrackId2 = SpotifyTrackId.randomId()
        def spotifyAlbumId = SpotifyAlbumId.randomId()

        spotifyMock.mockSpotifyAuthorization()

        def spotifyAlbumResponse =
                """{
                    "id": "${spotifyAlbumId}",
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
                                "id": "${spotifyTrackId1}",
                                "name": "track1",
                                "artists": [
                                    {"name": "artist1"},
                                    {"name": "artist2"}
                                ]
                            },
                            {
                                "id": "${spotifyTrackId2}",
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
                spotifyAlbumId,
                spotifyAlbumResponse
        )

        def audioFeaturesResponse =
                """{   
                    "audio_features": [
                    {
                        "id":  "${spotifyTrackId1}",
                        "tempo": 131.054
                    },
                    {
                        "id": "${spotifyTrackId2}",
                        "tempo": 91.345
                    }
                    ]
                }"""
        spotifyMock.mockGetAudioFeatures(
                once(),
                List.of(spotifyTrackId1, spotifyTrackId2),
                audioFeaturesResponse
        )

        expect:
        def expectedResponse =
                """{
                    "spotifyId": "${spotifyAlbumId}",
                    "name": "first spotify album",
                    "artists": [
                        "artist1",
                        "artist2"
                    ],
                    "spotifyTracks": [
                        {
                            "spotifyId": "${spotifyTrackId1}",
                            "name": "track1",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "audioFeatures": {
                                "id": "${spotifyTrackId1}",
                                "tempo": 131.05
                            }
                        },
                        {
                            "spotifyId": "${spotifyTrackId2}",
                            "name": "track2",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "audioFeatures": {
                                "id": "${spotifyTrackId2}",
                                "tempo": 91.35
                            }
                        }
                    ],
                    "imagePath": "imagePath1"
                }"""

        client.get()
                .uri("/api/spotify/albums/" + spotifyAlbumId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET spotify/albums/{id} should return 404 when spotify album cannot be found"() {
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
        client.get()
                .uri("/api/spotify/albums/" + spotifyAlbumId)
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

    def "GET spotify/playlists/{id} should return spotify album with tracks and audio features"() {
        given:
        def spotifyTrackId1 = SpotifyTrackId.randomId()
        def spotifyTrackId2 = SpotifyTrackId.randomId()
        def spotifyAlbumId1 = SpotifyAlbumId.randomId()
        def spotifyAlbumId2 = SpotifyAlbumId.randomId()
        def spotifyPlaylistId = SpotifyPlaylistId.randomId()

        spotifyMock.mockSpotifyAuthorization()

        def spotifyPlaylistResponse =
                """{
                    "id": "${spotifyPlaylistId}",
                    "tracks": {
                        "items" : [
                            {
                                "track": {
                                    "id": "${spotifyTrackId1}",
                                    "name": "track1",
                                    "artists": [
                                        {"name": "artist1"},
                                        {"name": "artist2"}
                                    ],
                                    "album": {
                                        "id": "${spotifyAlbumId1}",
                                        "name": "Normal Bias album",
                                        "artists": [
                                            {"name" : "Normal Bias 6"}
                                        ],
                                        "images" : [
                                            {"url": "url1"},
                                            {"url": "url2"}
                                        ]
                                    }
                                }
                            },
                            {
                                "track": {
                                    "id": "${spotifyTrackId2}",
                                    "name": "track2",
                                    "artists": [
                                        {"name": "artist1"},
                                        {"name": "artist2"}
                                    ],
                                    "album": {
                                        "id": "${spotifyAlbumId2}",
                                        "name": "Normal Bias album",
                                        "artists": [
                                            {"name" : "Normal Bias 6"}
                                        ],
                                        "images" : [
                                            {"url": "url3"},
                                            {"url": "url4"}
                                        ]
                                    }
                                }
                            }
                        ]
                    }
                }"""
        spotifyMock.mockGetSpotifyPlaylist(
                twice(),
                spotifyPlaylistId,
                spotifyPlaylistResponse
        )

        expect:
        def expectedResponse =
                """{
                    "spotifyId": "${spotifyPlaylistId}",
                    "tracks": [
                        {
                            "spotifyId": "${spotifyTrackId1}",
                            "name": "track1",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "spotifyAlbum": {
                                "spotifyId": "${spotifyAlbumId1}",
                                "name": "Normal Bias album",
                                "artists": [
                                    "Normal Bias 6"
                                ],
                                "imagePath": "url1"
                            },
                            "imagePath": "url1"
                        },
                        {
                            "spotifyId": "${spotifyTrackId2}",
                            "name": "track2",
                            "artists": [
                                "artist1",
                                "artist2"
                            ],
                            "spotifyAlbum": {
                                "spotifyId": "${spotifyAlbumId2}",
                                "name": "Normal Bias album",
                                "artists": [
                                    "Normal Bias 6"
                                ],
                                "imagePath": "url3"
                            },
                            "imagePath": "url3"
                        }
                    ]
                }"""

        client.get()
                .uri("/api/spotify/playlists/" + spotifyPlaylistId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JsonNode.class)
                .value { assertJson(it.toString(), expectedResponse) }
    }

    def "GET spotify/playlists/{id} should return 404 when spotify playlist cannot be found"() {
        given:
        def spotifyPlaylistId = SpotifyPlaylistId.randomId()

        spotifyMock.mockSpotifyAuthorization()

        def errorResponse =
                """{
                    "error": {
                        "message" : "Non existing id: 'spotify:playlist:${spotifyPlaylistId}'"
                    }
                }"""
        spotifyMock.mockNotFoundErrorOnGetSpotifyPlaylist(
                once(),
                spotifyPlaylistId,
                errorResponse
        )

        expect:
        client.get()
                .uri("/api/spotify/playlists/" + spotifyPlaylistId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .value { message ->
                    assertThat(
                            message,
                            containsString("Non existing id: 'spotify:playlist:${spotifyPlaylistId}'")
                    )
                }
    }
}
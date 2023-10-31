package dev.nos.djnavigator.spotify

import dev.nos.djnavigator.spotify.client.SpotifyQueries
import dev.nos.djnavigator.spotify.model.SpotifyAlbum
import dev.nos.djnavigator.spotify.model.SpotifyTrack
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.ExpectedCount.once

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ActiveProfiles("test")
class SpotifyQueriesSpec extends Specification {

    @Autowired
    private SpotifyQueries spotifyQueries

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

    def "should trackWithAudioFeature return track with audio features"() {
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
        spotifyMock.mockGetAudioFeatures(once(), spotifyTrackId, audioFeaturesResponse)


        when:
        def actualSpotifyTrack = spotifyQueries.trackWithAudioFeature(spotifyTrackId)

        then:
        actualSpotifyTrack == SpotifyTrack.builder()
                .spotifyId(spotifyTrackId)
                .name("Embody Control")
                .artists(List.of("Normal Bias 1", "Normal Bias 2"))
                .audioFeatures(
                        SpotifyTrackAudioFeatures.builder()
                                .id(spotifyTrackId)
                                .tempo(new BigDecimal("131.05"))
                                .build()
                )
                .spotifyAlbum(
                        SpotifyAlbum.builder()
                                .name("Normal Bias album")
                                .spotifyId(spotifyAlbumId)
                                .imagePath("url1")
                                .artists(List.of("Normal Bias 3"))
                                .build()
                )
                .imagePath("url1")
                .build()
    }
}

package dev.nos.djnavigator.spotify

import dev.nos.djnavigator.spotify.config.SpotifyToken
import dev.nos.djnavigator.spotify.config.SpotifyTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static dev.nos.djnavigator.spotify.config.SpotifyToken.SPOTIFY_TOKEN_KEY

@SpringBootTest
@ContextConfiguration
@ActiveProfiles("test")
class SpotifyTokenRepositorySpec extends Specification {

    @Autowired
    SpotifyTokenRepository spotifyTokenRepository

    void cleanup() {
        spotifyTokenRepository.delete(SPOTIFY_TOKEN_KEY)
    }

    def "should save spotifyToken correctly"() {
        given:
        def now = new Date()
        def spotifyToken = new SpotifyToken("accessToken", now)

        when:
        spotifyTokenRepository.set(SPOTIFY_TOKEN_KEY, new SpotifyToken("accessToken", now))
        def result = spotifyTokenRepository.get(SPOTIFY_TOKEN_KEY)

        then:
        result == spotifyToken
    }

}

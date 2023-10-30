package dev.nos.djnavigator.repository

import dev.nos.djnavigator.collection.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static dev.nos.djnavigator.TestData.album

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ActiveProfiles("test")
class AlbumRepositorySpec extends Specification {

    @Autowired
    private AlbumRepository albumRepository

    void cleanup() {
        albumRepository.deleteAll()
    }

    @Transactional
    def "should findBySpotifyId return album"() {
        given:
        def album1 = albumRepository.save(
                album()
                        .spotifyId("spotifyId1")
                        .build()
        )
        albumRepository.save(
                album()
                        .spotifyId("spotifyId2")
                        .build()
        )

        when:
        def album = albumRepository.findBySpotifyId("spotifyId1")

        then:
        album.get() == album1
    }

    @Transactional
    def "should findBySpotifyId return empty optional when album is not exist"() {
        given:
        albumRepository.save(
                album()
                        .spotifyId("spotifyId")
                        .build()
        )

        when:
        def album = albumRepository.findBySpotifyId("xxx")

        then:
        album.isEmpty()
    }

}

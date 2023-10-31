package dev.nos.djnavigator.collection.repository

import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId
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
                        .spotifyId(AlbumSpotifyId.from("spotifyId1"))
                        .build()
        )
        albumRepository.save(
                album()
                        .spotifyId(AlbumSpotifyId.from("spotifyId2"))
                        .build()
        )

        when:
        def album = albumRepository.findBySpotifyId(AlbumSpotifyId.from("spotifyId1"))

        then:
        album.get() == album1
    }

    @Transactional
    def "should findBySpotifyId return empty optional when album is not exist"() {
        given:
        albumRepository.save(
                album()
                        .spotifyId(AlbumSpotifyId.from("spotifyId"))
                        .build()
        )

        when:
        def album = albumRepository.findBySpotifyId(AlbumSpotifyId.from("xxx"))

        then:
        album.isEmpty()
    }

}

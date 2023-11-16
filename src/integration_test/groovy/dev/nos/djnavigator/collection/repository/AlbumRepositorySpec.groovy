package dev.nos.djnavigator.collection.repository

import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static dev.nos.djnavigator.TestData.album
import static dev.nos.djnavigator.TestData.track

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ActiveProfiles("test")
class AlbumRepositorySpec extends Specification {

    @Autowired
    private TrackRepository trackRepository

    @Autowired
    private AlbumRepository albumRepository

    void cleanup() {
        trackRepository.deleteAll()
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

    @Transactional
    def "should deleteAlbum delete album with all tracks"() {
        given:
        def album = album().build()
        albumRepository.save(album)
        def track1 = track()
                .album(album)
                .build()
        def track2 = track()
                .album(album)
                .build()
        album.addTracks(List.of(track1, track2))
        albumRepository.save(album)

        def track3 = track()
                .album(album)
                .build()
        album.addTrack(track3)
        trackRepository.save(track3)

        when:
        albumRepository.delete(album)

        then:
        trackRepository.findById(track1.id).isEmpty()
        trackRepository.findById(track2.id).isEmpty()
        trackRepository.findById(track3.id).isEmpty()
        albumRepository.findById(album.id).isEmpty()
    }
}

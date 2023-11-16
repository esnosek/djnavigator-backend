package dev.nos.djnavigator.collection.repository


import dev.nos.djnavigator.collection.model.id.TrackSpotifyId
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
class TrackRepositorySpec extends Specification {

    @Autowired
    private TrackRepository trackRepository

    @Autowired
    private AlbumRepository albumRepository

    void cleanup() {
        trackRepository.deleteAll()
        albumRepository.deleteAll()
    }

    @Transactional
    def "should findByAlbumId return all album tracks"() {
        given:
        def album = albumRepository.save(album().build())
        def track1 = trackRepository.save(track().album(album).build())
        def track2 = trackRepository.save(track().album(album).build())
        trackRepository.save(track().build())

        when:
        def resultTracks = trackRepository.findByAlbumId(album.id)

        then:
        resultTracks == List.of(track1, track2)
    }

    @Transactional
    def "should findBySpotifyId return all album tracks"() {
        given:
        def album = albumRepository.save(album().build())
        def track1 = trackRepository.save(track().album(album).build())
        trackRepository.save(track().album(album).build())

        when:
        def resultTrack = trackRepository.findBySpotifyId(track1.spotifyId)

        then:
        resultTrack.get() == track1
    }

    @Transactional
    def "should findBySpotifyId return empty optional when track is not exist"() {
        given:
        def album = albumRepository.save(album().build())
        trackRepository.save(track().album(album).build())

        when:
        def resultTrack = trackRepository.findBySpotifyId(TrackSpotifyId.from("xxx"))

        then:
        resultTrack.isEmpty()
    }
}

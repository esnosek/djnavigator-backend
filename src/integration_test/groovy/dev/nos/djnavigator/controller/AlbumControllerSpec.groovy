package dev.nos.djnavigator.controller


import dev.nos.djnavigator.dto.AlbumView
import dev.nos.djnavigator.repository.AlbumRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static dev.nos.djnavigator.TestData.album
import static java.lang.String.format

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
class AlbumControllerSpec extends Specification {

    @Autowired
    private WebTestClient client

    @Autowired
    private AlbumRepository albumRepository

    def "should POST /api/albums save album and return album view"() {
        given:

        def albumCreateDtoJson =
                """{   
                    "name": "first album",
                    "artists": ["artist1", "artist2"]
                }"""

        expect:
        def albumView = client.post().uri("/api/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(parsedJson(albumCreateDtoJson))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AlbumView.class)
                .returnResult()
                .responseBody

        println albumView

        and:
        albumRepository.findById(albumView.id()).orElseThrow() != null

//                .jsonPath("id").isNotEmpty()
//                .jsonPath("createdDate").isNotEmpty()
//                .jsonPath("name").isEqualTo("first album")
//                .jsonPath("artists").isEqualTo(["artist1", "artist2"])
//                .jsonPath("tracks").isEqualTo([])
//                .jsonPath("length()").isEqualTo(5)


    }

    def "should GET /api/albums/{id} return album"() {
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
        client.get().uri(format("/api/albums/%s", album.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo(parsedJson(expectedResponse))
    }

    def "should GET /api/albums/{id} return 404 when album is not found in the collection"() {
        given:
        def album = album().build()

        expect:
        client.get().uri(format("/api/albums/%s", album.getId()))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("message")
                .isEqualTo("Album with id ${album.getId()} cannot be found in your collection".toString())
    }

    private String formatDate(LocalDateTime date) {
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return date.format(formatter)
    }

    private String parsedJson(String json) {
        def parsedJson = new JsonSlurper().parseText(json)
        return JsonOutput.toJson(parsedJson)
    }
}

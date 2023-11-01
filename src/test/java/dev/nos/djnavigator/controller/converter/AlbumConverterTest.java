package dev.nos.djnavigator.controller.converter;

import dev.nos.djnavigator.collection.controller.converter.model.AlbumConverter;
import dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nos.djnavigator.TestData.albumCreateDto;
import static dev.nos.djnavigator.TestData.spotifyAlbum;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AlbumConverterTest {

    private final Clock clock = mock(Clock.class);

    private final AlbumConverter albumConverter = new AlbumConverter(clock);

    @Test
    void should_ToAlbum_AlbumCreateDto_ReturnAlbum() {
        // given
        var albumCreateDto = albumCreateDto().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var album = albumConverter.toAlbum(albumCreateDto);

        // then
        assertThat(album)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(createdDate, from(Album::getCreatedDate))
                .returns(album.getArtists(), from(Album::getArtists))
                .returns(album.getName(), from(Album::getName))
                .returns(List.of(), from(Album::getTracks))
                .returns(null, from(Album::getSpotifyId));
    }

    @Test
    void should_ToAlbum_SpotifyAlbum_ReturnAlbum() {
        // given
        var spotifyAlbum = spotifyAlbum().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var album = albumConverter.toAlbum(spotifyAlbum);

        // then
        assertThat(album)
                .returns(true, from(exp -> exp.getId() != null))
                .returns(createdDate, from(Album::getCreatedDate))
                .returns(spotifyAlbum.artists(), from(Album::getArtists))
                .returns(spotifyAlbum.name(), from(Album::getName))
                .returns(List.of(), from(Album::getTracks))
                .returns(ConvertersUtils.toAlbumSpotifyId(spotifyAlbum.spotifyId()), from(Album::getSpotifyId));
    }

}
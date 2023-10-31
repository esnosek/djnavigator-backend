package dev.nos.djnavigator.controller.converter;

import dev.nos.djnavigator.collection.controller.converter.model.AlbumConverter;
import dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils;
import dev.nos.djnavigator.collection.model.Album;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nos.djnavigator.TestData.albumCreateDto;
import static dev.nos.djnavigator.TestData.spotifyAlbum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class AlbumConverterTest {

    private final AlbumConverter albumConverter = new AlbumConverter();

    @Test
    void should_ToAlbum_AlbumCreateDto_ReturnAlbum() {
        // given
        final var albumCreateDto = albumCreateDto().build();

        // when
        final var album = albumConverter.toAlbum(albumCreateDto);

        System.out.println(album.getId());
        System.out.println(album.getCreatedDate());

        //then
        assertThat(album)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(album.getArtists(), from(Album::getArtists))
                .returns(album.getName(), from(Album::getName))
                .returns(List.of(), from(Album::getTracks))
                .returns(null, from(Album::getSpotifyId));
    }

    @Test
    void should_ToAlbum_SpotifyAlbum_ReturnAlbum() {
        final var spotifyAlbum = spotifyAlbum().build();
        final var album = albumConverter.toAlbum(spotifyAlbum);
        assertThat(album)
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(spotifyAlbum.artists(), from(Album::getArtists))
                .returns(spotifyAlbum.name(), from(Album::getName))
                .returns(List.of(), from(Album::getTracks))
                .returns(ConvertersUtils.toAlbumSpotifyId(spotifyAlbum.spotifyId()), from(Album::getSpotifyId));
    }

}
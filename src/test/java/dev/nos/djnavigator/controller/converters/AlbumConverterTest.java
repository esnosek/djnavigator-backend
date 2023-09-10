package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.model.Album;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class AlbumConverterTest {

    private final AlbumCreator albumCreator = new AlbumCreator();

    @Test
    void should_ToAlbum_AlbumCreateDto_ReturnAlbum() {
        // given
        final var albumCreateDto = albumCreateDto().build();

        // when
        final var album = albumCreator.createAlbum(albumCreateDto);

        //then
        assertThat(album)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(album.getArtists(), from(Album::getArtists))
                .returns(album.getName(), from(Album::getName))
                .returns(null, from(Album::getTracks))
                .returns(null, from(Album::getSpotifyId));
    }

    @Test
    void should_ToAlbum_SpotifyAlbum_ReturnAlbum() {
        final var spotifyAlbum = spotifyAlbum().build();
        final var album = albumCreator.createAlbum(spotifyAlbum);
        assertThat(album)
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(spotifyAlbum.artists(), from(Album::getArtists))
                .returns(spotifyAlbum.name(), from(Album::getName))
                .returns(null, from(Album::getTracks))
                .returns(spotifyAlbum.spotifyId(), from(Album::getSpotifyId));
    }

    @Test
    void should_ToAlbumView_Album_ReturnAlbumView() {
        // given
        final var album = album().build();

        // when
        final var albumView = albumCreator.toAlbumView(album);

        // then
        assertThat(albumView)
                .returns(album.getId(), from(AlbumView::id))
                .returns(album.getCreatedDate(), from(AlbumView::createdDate))
                .returns(album.getArtists(), from(AlbumView::artists))
                .returns(album.getName(), from(AlbumView::name))
                .returns(null, from(AlbumView::tracks))
                .returns(album.getSpotifyId(), from(AlbumView::spotifyId));
    }
}
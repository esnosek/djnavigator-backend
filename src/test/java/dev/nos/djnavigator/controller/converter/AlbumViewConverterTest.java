package dev.nos.djnavigator.controller.converter;

import dev.nos.djnavigator.collection.dto.AlbumView;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.album;
import static dev.nos.djnavigator.collection.controller.converter.view.AlbumViewConverter.toAlbumView;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class AlbumViewConverterTest {

    @Test
    void should_ToAlbumView_Album_ReturnAlbumView() {
        // given
        final var album = album().build();

        // when
        final var albumView = toAlbumView(album, false);

        // then
        assertThat(albumView)
                .returns(album.getId().id(), from(AlbumView::id))
                .returns(album.getCreatedDate(), from(AlbumView::createdDate))
                .returns(album.getArtists(), from(AlbumView::artists))
                .returns(album.getName(), from(AlbumView::name))
                .returns(null, from(AlbumView::tracks))
                .returns(album.getSpotifyId(), from(AlbumView::spotifyId));
    }
}
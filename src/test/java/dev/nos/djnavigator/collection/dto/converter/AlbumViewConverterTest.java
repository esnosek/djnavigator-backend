package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.AlbumView;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.album;
import static dev.nos.djnavigator.collection.dto.converter.AlbumViewConverter.albumView;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class AlbumViewConverterTest {

    @Test
    void should_ToAlbumView_Album_ReturnAlbumView() {
        // given
        var album = album().build();

        // when
        var albumView = albumView(album, false);

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
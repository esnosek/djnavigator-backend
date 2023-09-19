package dev.nos.djnavigator.controller.creator;

import dev.nos.djnavigator.controller.creator.view.AlbumViewCreator;
import dev.nos.djnavigator.dto.AlbumView;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.album;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class AlbumViewCreatorTest {

    private final AlbumViewCreator albumViewCreator = new AlbumViewCreator();

    @Test
    void should_ToAlbumView_Album_ReturnAlbumView() {
        // given
        final var album = album().build();

        // when
        final var albumView = albumViewCreator.toAlbumView(album);

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
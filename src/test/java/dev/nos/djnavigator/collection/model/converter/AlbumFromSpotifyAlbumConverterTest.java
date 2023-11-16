package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.utils.ConvertersUtils;
import dev.nos.djnavigator.utils.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nos.djnavigator.TestData.spotifyAlbum;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AlbumFromSpotifyAlbumConverterTest {

    private final Clock clock = mock(Clock.class);

    private final AlbumFromSpotifyAlbumConverter albumFromSpotifyAlbumConverter = new AlbumFromSpotifyAlbumConverter(clock);

    @Test
    void should_apply_SpotifyAlbum_ReturnAlbum() {
        // given
        var spotifyAlbum = spotifyAlbum().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var album = albumFromSpotifyAlbumConverter.apply(spotifyAlbum);

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
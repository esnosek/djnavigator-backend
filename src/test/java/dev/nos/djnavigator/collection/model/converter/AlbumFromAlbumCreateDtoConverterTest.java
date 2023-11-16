package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.utils.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nos.djnavigator.TestData.albumCreateDto;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AlbumFromAlbumCreateDtoConverterTest {

    private final Clock clock = mock(Clock.class);

    private final AlbumFromAlbumCreateDtoConverter albumFromAlbumCreateDtoConverter = new AlbumFromAlbumCreateDtoConverter(clock);

    @Test
    void should_apply_AlbumCreateDto_ReturnAlbum() {
        // given
        var albumCreateDto = albumCreateDto().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var album = albumFromAlbumCreateDtoConverter.apply(albumCreateDto);

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

}
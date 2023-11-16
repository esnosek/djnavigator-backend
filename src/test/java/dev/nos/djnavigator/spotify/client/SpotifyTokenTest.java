package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.config.SpotifyToken;
import dev.nos.djnavigator.utils.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SpotifyTokenTest {

    @Test
    void should_needToRefresh_return_false_when_expirationDate_is_60s_to_expired() {
        // given
        var now = Instant.now();
        var spotifyToken = new SpotifyToken("tokenValue", Date.from(now));
        var clock = mock(Clock.class);

        given(clock.instant()).willReturn(now.minusSeconds(60));

        // when
        var needToRefresh = spotifyToken.needToRefresh(clock);

        // then
        assertThat(needToRefresh, is(false));
    }

    @Test
    void should_needToRefresh_return_true_when_expirationDate_is_less_then_60s_to_expired() {
        // given
        var now = Instant.now();
        var spotifyToken = new SpotifyToken("tokenValue", Date.from(now));
        var clock = mock(Clock.class);

        given(clock.instant()).willReturn(now.minusSeconds(59));

        // when
        var needToRefresh = spotifyToken.needToRefresh(clock);

        // then
        assertThat(needToRefresh, is(true));
    }
}
package dev.nos.djnavigator.spotify.client;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class OAuthTokenTest {

    @Test
    void should_needToRefresh_return_false_when_expirationDate_is_60s_to_expired() {
        // given
        var now = Instant.now();
        var oAuthToken = new OAuthToken("tokenValue", Date.from(now));

        // when
        var needToRefresh = oAuthToken.needToRefresh(now.minusSeconds(60));

        // then
        assertThat(needToRefresh, is(false));
    }

    @Test
    void should_needToRefresh_return_true_when_expirationDate_is_less_then_60s_to_expired() {
        // given
        var now = Instant.now();
        var oAuthToken = new OAuthToken("tokenValue", Date.from(now));

        // when
        var needToRefresh = oAuthToken.needToRefresh(
                now.minusSeconds(59)
        );

        // then
        assertThat(needToRefresh, is(true));
    }
}
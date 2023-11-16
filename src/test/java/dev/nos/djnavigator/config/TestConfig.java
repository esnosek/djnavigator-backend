package dev.nos.djnavigator.config;

import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Instant;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public Clock clock() {
        return new TestClockImpl(Instant.now());
    }
}

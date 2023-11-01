package dev.nos.djnavigator.collection.config;

import dev.nos.djnavigator.time.Clock;
import dev.nos.djnavigator.time.ClockImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CollectionConfig {

    @Bean
    public Clock clock() {
        return new ClockImpl(java.time.Clock.systemDefaultZone());
    }
}

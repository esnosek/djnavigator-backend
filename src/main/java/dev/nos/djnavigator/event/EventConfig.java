package dev.nos.djnavigator.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventConfig {

    @Bean
    EventManager eventManager(EventManagerFactory eventManagerFactory) {
        return eventManagerFactory.createEventManager();
    }

}

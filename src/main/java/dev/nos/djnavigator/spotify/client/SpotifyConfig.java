package dev.nos.djnavigator.spotify.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
class SpotifyConfig {

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    SpotifyCredentials spotifyCredentials(
            @Value("${spotify.clientId}") String clientId,
            @Value("${spotify.clientSecret}") String clientSecret) {
        return new SpotifyCredentials(clientId, clientSecret);
    }
}

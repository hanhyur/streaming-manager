package me.owen.streaming.streamingmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfig {

    @Bean
    public WebClient youtubeWebClient() {
        return WebClient.builder()
                .baseUrl("https://www.googleapis.com/youtube/v3")
                .build();
    }

    @Bean
    public WebClient twitchWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.twitch.tv/helix")
                .build();
    }

}

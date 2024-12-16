package me.owen.streaming.streamingmanager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TwitchOAuth2Config {

    @Value("${twitch.oauth2.client-id}")
    private String clientId;

    @Value("${twitch.oauth2.client-secret}")
    private String clientSecret;

    @Value("${twitch.oauth2.redirect-uri}")
    private String redirectUri;

    @Bean
    public String twitchAuthorizationUrl() {
        return "https://id.twitch.tv/oauth2/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=user:read:subscriptions user:read:email";
    }

}

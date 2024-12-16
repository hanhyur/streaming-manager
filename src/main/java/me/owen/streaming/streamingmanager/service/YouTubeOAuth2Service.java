package me.owen.streaming.streamingmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class YouTubeOAuth2Service {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com/token")
            .build();

    public String exchangeCodeForToken(String code, String clientId, String clientSecret, String redirectUri) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("code", code)
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

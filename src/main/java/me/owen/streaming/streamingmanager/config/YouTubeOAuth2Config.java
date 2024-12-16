package me.owen.streaming.streamingmanager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class YouTubeOAuth2Config {

    @Value("{youtube.oauth2.client-id}")
    private String clientId;

    @Value("{youtube.oauth2.client-secret}")
    private String clientSecret;

    @Value("{youtube.oauth2.redirect-uri}")
    private String redirectUri;

    public String youtubeAuthorizationUrl() {
        return "https://www.youtube.com/oauth2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/youtube.readonly";
    }

}

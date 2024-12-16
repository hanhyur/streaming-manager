package me.owen.streaming.streamingmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.dto.TwitchDashboardData;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TwitchService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.twitch.tv/helix")
            .defaultHeader("Client-Id", "YOUR_TWITCH_CLIENT_ID")
            .build();

    public TwitchDashboardData getDashboardData(String accessToken) {
        String userId = getUserId(accessToken);
        JsonNode videos = getVideos(accessToken, userId);

        JsonNode mostViewedVideo = StreamSupport.stream(videos.get("data").spliterator(), false)
                .max(Comparator.comparing(video -> video.get("view_count").asInt()))
                .orElse(null);

        JsonNode leastViewedVideo = StreamSupport.stream(videos.get("data").spliterator(), false)
                .min(Comparator.comparing(video -> video.get("view_count").asInt()))
                .orElse(null);

        return new TwitchDashboardData(
                getJoinedDate(accessToken, userId),
                getSubscriberCount(accessToken, userId),
                mostViewedVideo != null ? mostViewedVideo.get("title").asText() : "N/A",
                mostViewedVideo != null ? mostViewedVideo.get("created_at").asText() : "N/A",
                leastViewedVideo != null ? leastViewedVideo.get("title").asText() : "N/A",
                leastViewedVideo != null ? leastViewedVideo.get("created_at").asText() : "N/A"
        );
    }

    private String getUserId(String accessToken) {
        JsonNode response = webClient.get()
                .uri("/users")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response.get("data").get(0).get("id").asText();
    }

    private JsonNode getVideos(String accessToken, String userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("user_id", userId)
                        .queryParam("type", "archive")
                        .queryParam("first", 50)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private long getSubscriberCount(String accessToken, String userId) {
        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/subscriptions")
                        .queryParam("broadcaster_id", userId)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response != null && response.has("total") ? response.get("total").asLong() : 0;
    }

    private String getJoinedDate(String accessToken, String userId) {
        JsonNode response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("id", userId)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response.get("data").get(0).get("created_at").asText();
    }
}

package me.owen.streaming.streamingmanager.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.dto.YouTubeDashboardData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private YouTube getYouTubeClient() {
        return new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                JSON_FACTORY,
                request -> {}
        ).setApplicationName("StreamingManager").build();
    }

    public YouTubeDashboardData getDashboardData(String accessToken) throws IOException {
        YouTube youtube = getYouTubeClient();

        // 채널 정보 가져오기
        YouTube.Channels.List channelRequest = youtube.channels().list("snippet,statistics");
        channelRequest.setOauthToken(accessToken); // OAuth2 인증 토큰
        ChannelListResponse channelResponse = channelRequest.execute();

        if (channelResponse.getItems() == null || channelResponse.getItems().isEmpty()) {
            throw new IllegalArgumentException("채널 정보를 가져올 수 없습니다.");
        }

        Channel channel = channelResponse.getItems().get(0);

        // 동영상 정보 가져오기
        List<Video> videos = getVideos(accessToken);

        // 조회수 기준 정렬
        Optional<Video> mostViewedVideo = videos.stream()
                .max(Comparator.comparing(video -> video.getStatistics().getViewCount()));
        Optional<Video> leastViewedVideo = videos.stream()
                .min(Comparator.comparing(video -> video.getStatistics().getViewCount()));

        return new YouTubeDashboardData(
                channel.getSnippet().getPublishedAt().toString(),
                channel.getStatistics().getSubscriberCount(),
                mostViewedVideo.map(video -> video.getSnippet().getTitle()).orElse("N/A"),
                mostViewedVideo.map(video -> video.getSnippet().getPublishedAt().toString()).orElse("N/A"),
                leastViewedVideo.map(video -> video.getSnippet().getTitle()).orElse("N/A"),
                leastViewedVideo.map(video -> video.getSnippet().getPublishedAt().toString()).orElse("N/A")
        );
    }

    private List<Video> getVideos(String accessToken) throws IOException {
        YouTube youtube = getYouTubeClient();

        YouTube.Search.List searchRequest = youtube.search().list("id");
        searchRequest.setOauthToken(accessToken);
        searchRequest.setMaxResults(50L);
        searchRequest.setType("video");
        searchRequest.setForMine(true);

        SearchListResponse searchResponse = searchRequest.execute();
        List<String> videoIds = searchResponse.getItems().stream()
                .map(item -> item.getId().getVideoId())
                .toList();

        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics");
        videoRequest.setOauthToken(accessToken);
        videoRequest.setId(String.join(",", videoIds));

        VideoListResponse videoResponse = videoRequest.execute();
        return videoResponse.getItems();
    }

}

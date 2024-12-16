package me.owen.streaming.streamingmanager.service;

import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.dto.TwitchDashboardData;
import me.owen.streaming.streamingmanager.dto.YouTubeDashboardData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final YouTubeService youTubeService;
    private final TwitchService twitchService;

    public Map<String, Object> getDashboardData(String youtubeToken, String twitchToken) throws Exception {
        YouTubeDashboardData youtubeData = youTubeService.getDashboardData(youtubeToken);
        TwitchDashboardData twitchData = twitchService.getDashboardData(twitchToken);

        return Map.of(
                "YouTube", youtubeData,
                "Twitch", twitchData
        );
    }

}

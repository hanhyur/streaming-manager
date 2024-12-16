package me.owen.streaming.streamingmanager.controller;

import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestHeader("YouTube-Token") String youtubeToken,
            @RequestHeader("Twitch-Token") String twitchToken) throws Exception {
        Map<String, Object> data = dashboardService.getDashboardData(youtubeToken, twitchToken);
        return ResponseEntity.ok(data);
    }

}

package me.owen.streaming.streamingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TwitchDashboardData {

    private String joinedDate;

    private long subscriberCount;

    private String mostViewedVideoTitle;

    private String mostViewedVideoDate;

    private String leastViewedVideoTitle;

    private String leastViewedVideoDate;

}

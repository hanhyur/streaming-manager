package me.owen.streaming.streamingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class YouTubeDashboardData {

    private String joinedDate;

    private BigInteger subscriberCount;

    private String mostViewedVideoTitle;

    private String mostViewedVideoDate;

    private String leastViewedVideoTitle;

    private String leastViewedVideoDate;

}

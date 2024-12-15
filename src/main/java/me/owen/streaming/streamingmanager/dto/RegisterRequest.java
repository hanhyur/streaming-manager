package me.owen.streaming.streamingmanager.dto;

import lombok.Builder;
import me.owen.streaming.streamingmanager.domain.SocialType;

public record RegisterRequest(
        String email,
        String password,
        SocialType socialType
) {
    @Builder
    public RegisterRequest {}
}

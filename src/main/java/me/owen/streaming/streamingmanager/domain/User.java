package me.owen.streaming.streamingmanager.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.owen.streaming.streamingmanager.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private boolean emailVerified;

    private String verificationToken;

    private LocalDateTime deactivatedAt;

    @Column(unique = true)
    private String sub; // Google user unique ID

    @Builder
    public User(String email, String password, SocialType socialType, String sub) {
        this.email = email;
        this.password = password;
        this.socialType = socialType != null ? socialType : SocialType.NONE;
        this.emailVerified = false;
        this.verificationToken = UUID.randomUUID().toString();

        this.sub = sub;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.verificationToken = null;
    }

    public void deactivate() {
        this.deactivatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return deactivatedAt == null;
    }

}

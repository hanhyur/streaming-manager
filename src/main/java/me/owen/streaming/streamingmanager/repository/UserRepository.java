package me.owen.streaming.streamingmanager.repository;

import me.owen.streaming.streamingmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByEmailAndDeactivatedAtIsNull(String email);

    Optional<User> findBySub(String sub); // Google 사용자 검색

}

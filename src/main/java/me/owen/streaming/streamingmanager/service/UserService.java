package me.owen.streaming.streamingmanager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.domain.User;
import me.owen.streaming.streamingmanager.dto.RegisterRequest;
import me.owen.streaming.streamingmanager.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public User registerUser(RegisterRequest request) {
        userRepository.findByEmailAndDeactivatedAtIsNull(request.email())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .socialType(request.socialType())
                .build();

        User savedUser = userRepository.save(newUser);

        sendVerificationEmail(savedUser);

        return savedUser;
    }

    public void verityEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        user.verifyEmail();

        userRepository.save(user);
    }

    private void sendVerificationEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification");
            helper.setText("Click the link to verify your email : " +
                    "http://localhost:8080/api/users/verify?token=" + user.getVerificationToken());

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        user.deactivate();

        userRepository.save(user);
    }

}

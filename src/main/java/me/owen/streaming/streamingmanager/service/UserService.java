package me.owen.streaming.streamingmanager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.domain.SocialType;
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

    @Transactional
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

    @Transactional
    public String verityEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        user.verifyEmail();

        userRepository.save(user);

        return "이메일 인증이 완료되었습니다.";
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

    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        user.deactivate();

        userRepository.save(user);
    }

    @Transactional
    public User processGoogleUser(String email, String sub) {
        return userRepository.findBySub(sub)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .password("") // Google 이용자는 password가 필요하지 않음
                            .socialType(SocialType.GOOGLE)
                            .sub(sub)
                            .build();

                    return userRepository.save(newUser);
                });
    }

}

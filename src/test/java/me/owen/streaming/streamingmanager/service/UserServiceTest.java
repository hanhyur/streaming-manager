package me.owen.streaming.streamingmanager.service;

import jakarta.mail.internet.MimeMessage;
import me.owen.streaming.streamingmanager.domain.SocialType;
import me.owen.streaming.streamingmanager.domain.User;
import me.owen.streaming.streamingmanager.dto.RegisterRequest;
import me.owen.streaming.streamingmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_WithNewEmail_Success() {
        //given
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";

        RegisterRequest request = new RegisterRequest(email, password, SocialType.NONE);

        User newUser = User.builder()
                .email(email)
                .password(encodedPassword)
                .socialType(SocialType.NONE)
                .build();

        when(userRepository.findByEmailAndDeactivatedAtIsNull(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        //when
        User savedUser = userService.registerUser(request);

        //then
        assertNotNull(savedUser);
        assertEquals(email, savedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void registerUser_WithDuplicateEmail_ThrowsException() {
        // given
        String email = "duplicate@example.com";
        RegisterRequest request = new RegisterRequest(email, "password123", SocialType.NONE);

        when(userRepository.findByEmailAndDeactivatedAtIsNull(email)).thenReturn(Optional.of(new User()));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

}
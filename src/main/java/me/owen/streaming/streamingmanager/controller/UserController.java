package me.owen.streaming.streamingmanager.controller;

import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.domain.User;
import me.owen.streaming.streamingmanager.dto.RegisterRequest;
import me.owen.streaming.streamingmanager.dto.RegisterResponse;
import me.owen.streaming.streamingmanager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);

        return ResponseEntity.ok(new RegisterResponse("회원가입 신청이 완료되었습니다. 인증 메일을 확인하세요."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String message = userService.verityEmail(token);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);

        return ResponseEntity.ok("탈퇴 처리가 완료되었습니다.");
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(401).body(null);
        }

        String email = oAuth2User.getAttribute("email");
        User user = userService.processGoogleUser(email, oAuth2User.getAttribute("sub"));

        return ResponseEntity.ok(user);
    }

}

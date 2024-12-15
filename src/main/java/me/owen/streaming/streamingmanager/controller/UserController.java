package me.owen.streaming.streamingmanager.controller;

import lombok.RequiredArgsConstructor;
import me.owen.streaming.streamingmanager.dto.RegisterRequest;
import me.owen.streaming.streamingmanager.dto.RegisterResponse;
import me.owen.streaming.streamingmanager.service.UserService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        userService.verityEmail(token);

        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);

        return ResponseEntity.ok("탈퇴 처리가 완료되었습니다.");
    }

}

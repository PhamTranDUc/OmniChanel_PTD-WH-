package com.example.omnichannelfinal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserChatController {

    private final UserChatService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserChat addUser(
            @Payload UserChat user
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public UserChat disconnectUser(
            @Payload UserChat user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserChat>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}

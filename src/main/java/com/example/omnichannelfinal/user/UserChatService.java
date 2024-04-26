package com.example.omnichannelfinal.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChatService {

    private final UserChatRepository repository;

    public void saveUser(UserChat user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(UserChat user) {
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<UserChat> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
}

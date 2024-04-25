package com.example.omnichannelfinal.service;

import com.example.omnichannelfinal.dto.UserDto;
import com.example.omnichannelfinal.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    void createUser(UserDto userDto)throws Exception;
    ResponseEntity<?> login(String userName, String passWord) throws Exception;

    String getRoleByToken(String token);

    User getUserByToken(String token);

    List<User> getAllAccount();

    User updatePassword(String newPassword,String token);
}

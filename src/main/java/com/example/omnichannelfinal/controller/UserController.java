package com.example.omnichannelfinal.controller;

import com.example.omnichannelfinal.dto.PasswordUpdateDto;
import com.example.omnichannelfinal.dto.UserDto;
import com.example.omnichannelfinal.dto.UserLoginDto;
import com.example.omnichannelfinal.entity.User;
import com.example.omnichannelfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto, BindingResult result){
        try {
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body("Some Error!");
            }
            userService.createUser(userDto);
            return ResponseEntity.ok(userDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) throws Exception{
        return userService.login(userLoginDto.getUserName(),userLoginDto.getPassword());
    }

    @PostMapping("/detail")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String token){

        try{
            token= token.substring(7);
//            String roleName= userService.getRoleByToken(token);
            User user= userService.getUserByToken(token);
            return ResponseEntity.ok().body(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token, @RequestBody PasswordUpdateDto password) {

        try {
            token = token.substring(7);
            User user = userService.getUserByToken(token);

            // Kiểm tra xem mật khẩu mới có trùng khớp với mật khẩu cũ hay không
            if (bCryptPasswordEncoder.matches(password.getPassword(), user.getPassword())) {
                // Mật khẩu cũ trùng khớp, tiếp tục cập nhật mật khẩu mới
//                User userAfterUpdate = userService.updatePassword(password.getPassword(), token);
                com.example.omnichannelfinal.response.ResponseEntity responseEntity= new com.example.omnichannelfinal.response.ResponseEntity("Password mới trùng với password cũ");
                return ResponseEntity.badRequest().body(responseEntity);
            } else {;
                User userAfterUpdate=userService.updatePassword(password.getPassword(),token);
                return ResponseEntity.ok().body(userAfterUpdate);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

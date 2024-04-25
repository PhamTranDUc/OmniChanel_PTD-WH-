package com.example.omnichannelfinal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/omni-chat")
    public ResponseEntity<?> getChatHomePage() throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body("Hello Omni-chanel");
    }
}

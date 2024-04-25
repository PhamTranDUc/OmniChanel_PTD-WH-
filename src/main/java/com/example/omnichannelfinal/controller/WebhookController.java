package com.example.omnichannelfinal.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private static final String VERIFY_TOKEN = "12345";
    @GetMapping
    public String handleGetRequest(@RequestParam("hub.mode") String mode,
                                   @RequestParam("hub.verify_token") String token,
                                   @RequestParam("hub.challenge") String challenge) {
        System.out.println("-------------- New Request GEThub.verify_token --------------");

        System.out.println("Body: mode=" + mode + ", token=" + token + ", challenge=" + challenge);

        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            System.out.println("WEBHOOK_VERIFIED");
            return challenge;
        } else {
            System.out.println("Responding with 403 Forbidden");
            return "403 Forbidden";
        }
    }
    @PostMapping
    public void handlePostRequest(@RequestBody String payload) {
        System.out.println("-------------- New Request POST --------------");

        System.out.println("Body:"+ payload);    }

}

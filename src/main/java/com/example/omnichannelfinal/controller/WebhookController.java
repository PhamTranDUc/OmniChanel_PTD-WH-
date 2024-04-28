package com.example.omnichannelfinal.controller;

import com.example.omnichannelfinal.chat.ChatController;
import com.example.omnichannelfinal.chat.ChatMessage;
import com.example.omnichannelfinal.chat.ChatMessageService;
import com.example.omnichannelfinal.dto.WebHookPayLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.core.io.support.SpringFactoriesLoader.FailureHandler.handleMessage;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private ChatMessageService chatMessageService;

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
    public void handlePostRequest(@RequestBody WebHookPayLoad payload) {
        System.out.println("-------------- New Request POST --------------");

        for (WebHookPayLoad.Entry entry : payload.getEntry()) {

            for (WebHookPayLoad.Messaging messaging : entry.getMessaging()) {

                System.out.println("Message ID: " + messaging.getMessage().getMid());
                System.out.println("Message Text: " + messaging.getMessage().getText());

                System.out.println("Sender ID: " + messaging.getSender().getId());

                System.out.println("Recipient ID: " + messaging.getRecipient().getId());

                long timestamp = messaging.getTimestamp();
                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
                System.out.println("Time: " + dateTime );

                ChatMessage chatMessage= new ChatMessage();

                chatMessage.setSenderId(messaging.getSender().getId());
                chatMessage.setRecipientId(messaging.getRecipient().getId());
                chatMessage.setContent(messaging.getMessage().getText());
//                chatMessage.setTimestamp(dateTime);

                chatMessageService.save(chatMessage);
                System.out.println(messaging);
            }
        }

        System.out.println("Body:"+ payload);    }




}

package com.example.omnichannelfinal.chat;

import com.example.omnichannelfinal.constants.AuthenToken;
import com.example.omnichannelfinal.facebook.BoxChat;
import com.example.omnichannelfinal.facebook.FacebookService;
import com.example.omnichannelfinal.facebook.ResponseInforBoxChat;
import com.example.omnichannelfinal.facebook.ResponseListBoxChat;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    private  final MessageFaceBookService messageFaceBookService;
    private final FacebookService facebookService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        System.out.println(String.format("Nhận được tin nhắn từ %s đến %s với nội dung là %s",chatMessage.getSenderId(),chatMessage.getRecipientId(),chatMessage.getContent()));
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
//        messageFaceBookService.sendMessageById(chatMessage.getRecipientId(),chatMessage.getContent());
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }



    // send message from page to customer with facebook
    @PostMapping("/sendMessenger/{id}")
    public ResponseEntity<String> sendMessage(@PathVariable("id") String id, @RequestParam("message") String message) {

        messageFaceBookService.sendMessageById(id,message);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }




    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }


    @GetMapping("/onmi-chanel")
    public ResponseEntity<?> GetAllBoxChatFacebookById() throws Exception{
        List<BoxChat> boxChatList= facebookService.getListAllBoxChatFacebookByPageId();
        return ResponseEntity
                .ok(boxChatList);
    }

    @GetMapping("/hello")
    public ResponseEntity<?> test1() throws Exception{
        return ResponseEntity
                .ok("Ok");
    }

//    @GetMapping("/test2")
//    public ResponseEntity<String> testApi() {
//        try {
//            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.facebook.com/v19.0/t_447482397720185")
//                    .queryParam("fields", "messages{message,from,to,id}")
//                    .queryParam("access_token", AuthenToken.ACCESS_TOKEN);
//
//            HttpHeaders headers = new HttpHeaders();
//
//            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//
//            HttpEntity<?> entity = new HttpEntity<>(headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<String> response = restTemplate.exchange(
//                    builder.build().encode().toUri(),
//                    HttpMethod.GET,
//                    entity,
//                    String.class);
//
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}

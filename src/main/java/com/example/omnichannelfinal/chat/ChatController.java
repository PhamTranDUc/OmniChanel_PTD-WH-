package com.example.omnichannelfinal.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    private  final MessageFaceBookService messageFaceBookService;

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
}

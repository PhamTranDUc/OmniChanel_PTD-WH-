package com.example.omnichannelfinal.chat;


import com.example.omnichannelfinal.constants.AuthenToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageFaceBookService {


    public void sendMessageById(String id,String messenger){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AuthenToken.ACCESS_TOKEN);

        String SEND_API_URL="https://graph.facebook.com/v19.0/me/messages";
        String requestBody = "{"
                + "\"recipient\":{\"id\":\"" + id + "\"},"
                + "\"message\":{\"text\":\""+ messenger+ "\"}"
                + "}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(SEND_API_URL, request, String.class);
    }
}
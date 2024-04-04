package com.example.chatapptrpo.controllers;

import com.example.chatapptrpo.dto.*;
import com.example.chatapptrpo.sevices.ChatService;
import com.example.chatapptrpo.sevices.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatService chatService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/loadChats")
    public List<ChatResponse> getChats() {
        return chatService.getAllChats();
    }


    @PostMapping("/loadChatsByToken")
    public List<ChatListResponse> getChatsByToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return chatService.getChatsByToken(tokenRequest);
    }

    @PostMapping("/loadChatById")
    public ChatResponse loadChatById(@RequestBody @Valid ChatByIdRequest chatByIdRequest) {
        return chatService.getChatResponseById(chatByIdRequest);
    }


    @PostMapping("/create")
    public ChatResponse createChat(@RequestBody @Valid ChatRequest chatRequest) {
        return chatService.save(chatRequest);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteChat(@RequestBody @Valid Long id) {
        chatService.deleteById(id);
        return new ResponseEntity<>("Chat delete successful", OK);
    }

    /*@PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        messageService.sendMessage(userPrincipal.getUser(), messageRequest.getContent());
        return ResponseEntity.ok("Сообщение отправлено успешно!");
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageResponse>> getMessages() {
        List<MessageResponse> messages = messageService.getChatMessages();
        return ResponseEntity.ok(messages);
    }*/
}


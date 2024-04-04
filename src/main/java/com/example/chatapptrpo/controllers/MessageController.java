package com.example.chatapptrpo.controllers;

import com.example.chatapptrpo.dto.ChatByIdRequest;
import com.example.chatapptrpo.dto.ChatRequest;
import com.example.chatapptrpo.dto.MessageRequest;
import com.example.chatapptrpo.dto.MessageResponse;
import com.example.chatapptrpo.sevices.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/message")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody @Valid MessageRequest messageRequest) {
        messageService.save(messageRequest);
        return new ResponseEntity<>("Message send successful", OK);
    }


    @PostMapping("/loadMessagesByChat")
    public List<MessageResponse> loadMessagesByChat(@RequestBody @Valid ChatByIdRequest chatByIdRequest) {
        return messageService.getChatMessagesToResponse(chatByIdRequest);
    }


}

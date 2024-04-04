package com.example.chatapptrpo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponse {

    private Long messageId;
    private String content;
    private String username;
    private Long userId;
    private LocalDateTime timestamp;

}


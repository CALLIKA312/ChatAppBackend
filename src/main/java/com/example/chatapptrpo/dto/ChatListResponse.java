package com.example.chatapptrpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatListResponse {
    private Long chatId;
    private String companionUsername;
    private String lastMessage;
    private LocalDateTime timestamp;
}

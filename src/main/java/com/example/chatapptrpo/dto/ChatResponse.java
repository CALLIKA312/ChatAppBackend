package com.example.chatapptrpo.dto;

import com.example.chatapptrpo.entities.Message;
import com.example.chatapptrpo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private Long chatId;
    private List<Long> userIds;
    private List<String> usernames;
    private List<MessageResponse> messages;
}

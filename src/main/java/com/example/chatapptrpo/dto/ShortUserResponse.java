package com.example.chatapptrpo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortUserResponse {
    private Long userId;
    private String username;
}

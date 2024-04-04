package com.example.chatapptrpo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class ChangePasswordRequest {
        private String token;
        private String password;
}

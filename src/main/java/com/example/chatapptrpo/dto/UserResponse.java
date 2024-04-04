package com.example.chatapptrpo.dto;

import com.example.chatapptrpo.entities.Chat;
import com.example.chatapptrpo.entities.Message;
import com.example.chatapptrpo.entities.Role;
import com.example.chatapptrpo.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String roles;

}

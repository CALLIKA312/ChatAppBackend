package com.example.chatapptrpo.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "user_chats")
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}


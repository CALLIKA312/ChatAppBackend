package com.example.chatapptrpo.sevices;

import com.example.chatapptrpo.dto.ChatByIdRequest;
import com.example.chatapptrpo.dto.MessageRequest;
import com.example.chatapptrpo.entities.Chat;
import com.example.chatapptrpo.entities.Message;
import com.example.chatapptrpo.entities.User;
import com.example.chatapptrpo.dto.MessageResponse;
import com.example.chatapptrpo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    @Lazy
    private ChatService chatService;

    @Autowired
    @Lazy
    private UserService userService;

    public List<MessageResponse> getChatMessagesToResponse(ChatByIdRequest chatByIdRequest) {
        Chat chat = chatService.getChatById(chatByIdRequest.getChatId());
        List<Message> messages = chat.getMessages().stream().toList();
        List<MessageResponse> messageResponses = messages.stream()
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getContent(),
                        message.getUser().getUsername(),
                        message.getUser().getId(),
                        message.getTimestamp()
                ))
                .sorted(Comparator.comparing(MessageResponse::getTimestamp))
                .toList();

        return messageResponses;
    }

    public void save(MessageRequest messageRequest) {
        User user = userService.getUserById(messageRequest.getUserId());
        Chat chat = chatService.getChatById(messageRequest.getChatId());
        Message message = new Message();
        message.setUser(user);
        message.setChat(chat);
        message.setContent(messageRequest.getContent());
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        chat.getMessages().add(message);
    }
}


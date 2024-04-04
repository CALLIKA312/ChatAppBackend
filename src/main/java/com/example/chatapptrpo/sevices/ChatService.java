package com.example.chatapptrpo.sevices;

import com.example.chatapptrpo.dto.*;
import com.example.chatapptrpo.entities.Chat;
import com.example.chatapptrpo.entities.Message;
import com.example.chatapptrpo.entities.User;
import com.example.chatapptrpo.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private JwtProvider jwtProvider;

    public List<ChatResponse> getAllChats() {
        return chatRepository.findAll()
                .stream()
                .map(this::chatToResponse)
                .collect(toList());
    }

    public List<ChatResponse> getUserChatsResponse(Long userId) {
        return chatRepository.findByUsersId(userId)
                .stream()
                .map(this::chatToResponse)
                .collect(toList());
    }

    public List<Chat> getUserChats(Long userId) {
        return new ArrayList<>(chatRepository.findByUsersId(userId));
    }

    private ChatResponse chatToResponse(Chat chat) {
        return ChatResponse.builder().chatId(chat.getId())
                .userIds(chat.getUsers().stream().map(User::getId).collect(Collectors.toList()))
                .usernames(chat.getUsers().stream().map(User::getUsername).collect(toList()))
                .messages(messageService.getChatMessagesToResponse(new ChatByIdRequest(chat.getId())))
                .build();
    }

    public Chat getChatById(Long chatId) {
        return chatRepository.getChatById(chatId);
    }

    public ChatResponse getChatResponseById(ChatByIdRequest chatByIdRequest) {
        return chatToResponse(getChatById(chatByIdRequest.getChatId()));
    }

    public ChatResponse save(ChatRequest chatRequest) {
        Long fromUserId = chatRequest.getFromUserId();
        Long targetUserId = chatRequest.getTargetUserId();
        List<Chat> curUserChats = getUserChats(fromUserId);
        for (Chat chat : curUserChats) {
            if (chat.getUsers().stream().map(User::getId).toList().get(0).equals(targetUserId)
                    ||
                    chat.getUsers().stream().map(User::getId).toList().get(1).equals(targetUserId)) {
                return chatToResponse(chat);
            }
        }

        Chat chat = new Chat();
        List<User> users = new ArrayList<>();
        users.add(userService.getUserById(fromUserId));
        users.add(userService.getUserById(targetUserId));
        chat.setUsers(users);
        chat.setMessages(new ArrayList<>());
        chatRepository.save(chat);
        for (User user : users) user.getChats().add(chat);
        return chatToResponse(chat);
    }

    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

    public List<ChatListResponse> getChatsByToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getToken();
        jwt = jwt.substring(7);
        String username = jwtProvider.extractUserName(jwt);
        User user = userService.getUserByUsername(username);
        List<ChatListResponse> chatListResponses = new ArrayList<>();
        for (Chat chat : getUserChats(user.getId())) {
            Long chatId = chat.getId();
            chat.getUsers().remove(user);
            String companionUsername = chat.getUsers().get(0).getUsername();
            Message lastMessage = new Message();
            if (chat.getMessages().size() > 0) {
                lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
            }
            String lastMessageContent = lastMessage.getContent();
            LocalDateTime timestamp = lastMessage.getTimestamp();
            chatListResponses.add(new ChatListResponse(chatId, companionUsername, lastMessageContent, timestamp));
        }
        return chatListResponses;
    }

}


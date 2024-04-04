package com.example.chatapptrpo.sevices;

import com.example.chatapptrpo.dto.ChatResponse;
import com.example.chatapptrpo.dto.ShortUserResponse;
import com.example.chatapptrpo.dto.TokenRequest;
import com.example.chatapptrpo.dto.UserResponse;
import com.example.chatapptrpo.entities.Role;
import com.example.chatapptrpo.entities.User;
import com.example.chatapptrpo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Autowired
    @Lazy
    private ChatService chatService;

    @Autowired
    private JwtProvider jwtProvider;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                getAuthorities("USER")
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }

    public UserResponse getUserByToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getToken();
        jwt = jwt.substring(7);
        String username = jwtProvider.extractUserName(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        return new UserResponse(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRoles().get(0).getName());
    }

    public List<ShortUserResponse> getUsersByToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getToken();
        jwt = jwt.substring(7);
        String username = jwtProvider.extractUserName(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        List<User> users = userRepository.findAll();
        users.remove(user);
        return usersToShortResponse(users);
    }


    private List<ShortUserResponse> usersToShortResponse(List<User> users) {
        List<ShortUserResponse> userResponses = new ArrayList<>();
        for (User user : users) userResponses.add(new ShortUserResponse(user.getId(), user.getUsername()));
        return userResponses;
    }
}
package com.example.chatapptrpo.controllers;

import com.example.chatapptrpo.dto.ShortUserResponse;
import com.example.chatapptrpo.dto.TokenRequest;
import com.example.chatapptrpo.dto.UserResponse;
import com.example.chatapptrpo.sevices.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loadUser")
    public UserResponse getUserByToken(@RequestBody TokenRequest tokenRequest) {
        return userService.getUserByToken(tokenRequest);
    }

    @PostMapping("/loadUsers")
    public List<ShortUserResponse> getUsersByToken(@RequestBody TokenRequest tokenRequest) {
        return userService.getUsersByToken(tokenRequest);
    }
}

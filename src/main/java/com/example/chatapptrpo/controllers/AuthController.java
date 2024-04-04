package com.example.chatapptrpo.controllers;

import com.example.chatapptrpo.dto.AuthenticationResponse;
import com.example.chatapptrpo.dto.ChangePasswordRequest;
import com.example.chatapptrpo.dto.LoginRequest;
import com.example.chatapptrpo.dto.SignupRequest;
import com.example.chatapptrpo.sevices.AuthService;
import com.example.chatapptrpo.sevices.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        authService.signUp(signupRequest);
        return new ResponseEntity<>("User registration successful", OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return new ResponseEntity<>("User change password successful", OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("User logout successful", OK);
    }


    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successully", OK);
    }
}

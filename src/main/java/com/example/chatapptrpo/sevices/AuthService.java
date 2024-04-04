package com.example.chatapptrpo.sevices;

import com.example.chatapptrpo.dto.AuthenticationResponse;
import com.example.chatapptrpo.dto.ChangePasswordRequest;
import com.example.chatapptrpo.dto.LoginRequest;
import com.example.chatapptrpo.dto.SignupRequest;
import com.example.chatapptrpo.entities.NotificationEmail;
import com.example.chatapptrpo.entities.Role;
import com.example.chatapptrpo.entities.User;
import com.example.chatapptrpo.entities.VerificationToken;
import com.example.chatapptrpo.repositories.RoleRepository;
import com.example.chatapptrpo.repositories.UserRepository;
import com.example.chatapptrpo.repositories.VerificationTokenRepository;
import com.example.chatapptrpo.exeptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.example.chatapptrpo.utils.Constants.ACTIVATION_EMAIL;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @Transactional
    public void signUp(SignupRequest signupRequest) {
        Role role = roleRepository.findByName("USER");
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encodePassword(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        user.setEnabled(false);
        user.setChats(new ArrayList<>());

        userRepository.save(user);

        String token = generateVerificationToken(user);

        String message = mailContentBuilder.build("Thank you for signing up, please click on the below url to activate your account : "
                + ACTIVATION_EMAIL + "/" + token);
        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String jwt = changePasswordRequest.getToken();
        jwt = jwt.substring(7);
        String username = jwtProvider.extractUserName(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        user.setPassword(encodePassword(changePasswordRequest.getPassword()));
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String username = loginRequest.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        String authenticationToken = jwtProvider.generateToken(user);
        return new AuthenticationResponse(authenticationToken, user.getId());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new MyExeption("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new MyExeption("User Not Found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }


}

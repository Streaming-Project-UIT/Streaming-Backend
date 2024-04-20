package com.programming.streaming.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.programming.streaming.entity.AuthUser;
import com.programming.streaming.repository.AuthUserRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
@RestController
@AllArgsConstructor
public class UserController {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;


    static class Email {
        private String email;

        public String getEmail() {
            return email;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/send-verification-email")
    public String sendVerificationEmail(@RequestBody String emailJson) {
        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println(emailJson);
        Gson gson = new Gson();
        Email emailObject = gson.fromJson(emailJson, Email.class);

        // Extract email value
        String email = emailObject.getEmail();

        System.out.println(email);
        System.out.println(email);
        message.setTo(email);
        System.out.println(email);
        message.setSubject("Xác thực đăng ký");
        String loginLink = "http://localhost:3000/login";
        message.setText("Xin chào, vui lòng nhấn vào liên kết sau để xác thực đăng ký: " + loginLink);

        try {
            javaMailSender.send(message);
            return "Email xác thực đã được gửi thành công";
        } catch (MailException e) {
            System.out.println(e.getMessage());
            return "Gửi email xác thực thất bại: " + e.getMessage();
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody AuthUser user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken. Please try again");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setAvatar(getDefaultAvatar());
            AuthUser save = userRepository.save(user);
            return ResponseEntity.ok(save);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    private byte[] getDefaultAvatar() throws IOException {
        String defaultAvatarPath = "image-1.png"; // Replace with the actual path to the default avatar
                                                                 // image
        Path path = Paths.get(defaultAvatarPath);
        return Files.readAllBytes(path);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login2")
    public ResponseEntity loginUser(@RequestBody AuthUser user) {
        try {
            AuthUser userFromDb = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new Exception("User not found"));
            if (passwordEncoder.matches(user.getPassword(), userFromDb.getPassword())) {
                return ResponseEntity.ok(userFromDb);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity logoutUser() {
        try {
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/listUser")
    public ResponseEntity listUser() {
        try {
            return ResponseEntity.ok(userRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/listUserbyUsername")
    public ResponseEntity listUserbyUsername(@RequestBody AuthUser user) {
        try {
            return ResponseEntity.ok(userRepository.findByUsername(user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/listUserbyId/{id}")
    public ResponseEntity listUserbyId(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(userRepository.findById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/updateProfile/{id}")
    public ResponseEntity updateProfile(@PathVariable("id") String id, @RequestBody AuthUser user) {
        try {
            AuthUser userFromDb = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("User not found"));

            userFromDb.setFirstName(user.getFirstName());
            userFromDb.setLastName(user.getLastName());
            // userFromDb.setPicture(user.getPicture());
            // userFromDb.setAvatar(uploadAvatar());
            AuthUser save = userRepository.save(userFromDb);

            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}

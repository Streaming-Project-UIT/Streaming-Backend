package com.programming.streaming.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.programming.streaming.entity.AdminAuthUser;
import com.programming.streaming.repository.Admin.AdminAuthUserRepository;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
public class AdminUserController {

    @Autowired
    private AdminAuthUserRepository adminAuthUserRepository;
    private final PasswordEncoder passwordEncoder;

    // //@SuppressWarnings("rawtypes")
    // @PostMapping("/admin/login")
    // public ResponseEntity loginAdmin(@RequestBody AdminAuthUser adminAuthUser) {
    // try {
    // AdminAuthUser adminAuthUserFromDb =
    // adminAuthUserRepository.findByUsername(adminAuthUser.getUsername())
    // .orElseThrow(() -> new Exception("Admin not found"));

    // // Kiểm tra mật khẩu bằng cách sử dụng passwordEncoder
    // if (passwordEncoder.matches(adminAuthUser.getPassword(),
    // adminAuthUserFromDb.getPassword())) {
    // return ResponseEntity.ok(adminAuthUserFromDb);
    // } else {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username
    // or password");
    // }
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username
    // or password");
    // }
    // }

    // @PostMapping("/admin/register")
    // public ResponseEntity registerAdmin(@RequestBody AdminAuthUser adminAuthUser)
    // {
    // try {
    // if
    // (adminAuthUserRepository.findByUsername(adminAuthUser.getUsername()).isPresent())
    // {
    // return ResponseEntity.badRequest().body("Username already exists");
    // }
    // adminAuthUser.setPassword(passwordEncoder.encode(adminAuthUser.getPassword()));
    // adminAuthUser.setUsername(adminAuthUser.getUsername());
    // AdminAuthUser save = adminAuthUserRepository.save(adminAuthUser);
    // return ResponseEntity.ok(save);

    // } catch (Exception e) {
    // return ResponseEntity.badRequest().body(e.getMessage());
    // }
    // }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminAuthUser adminAuthUser) {
        try {
            AdminAuthUser adminAuthUserFromDb = adminAuthUserRepository.findByUsername(adminAuthUser.getUsername())
                    .orElseThrow(() -> new Exception("Admin not found"));

            // Kiểm tra mật khẩu bằng cách sử dụng passwordEncoder
            if (passwordEncoder.matches(adminAuthUser.getPassword(), adminAuthUserFromDb.getPassword())) {
                return ResponseEntity.ok(adminAuthUserFromDb);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminAuthUser adminAuthUser) {
        try {
            if (adminAuthUserRepository.findByUsername(adminAuthUser.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            adminAuthUser.setPassword(passwordEncoder.encode(adminAuthUser.getPassword()));
            adminAuthUser.setUsername(adminAuthUser.getUsername());
            AdminAuthUser save = adminAuthUserRepository.save(adminAuthUser);
            return ResponseEntity.ok(save);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

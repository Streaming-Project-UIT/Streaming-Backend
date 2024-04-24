package com.programming.streaming.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import com.programming.streaming.entity.AdminAuthUser;
import com.programming.streaming.repository.Admin.AdminAuthUserRepository;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@AllArgsConstructor
public class AdminUserController {

    @Autowired
    private AdminAuthUserRepository adminAuthUserRepository;

    @PostMapping("/admin/login")
    public ResponseEntity loginAdmin(@RequestBody AdminAuthUser adminAuthUser) {
        try{
            AdminAuthUser adminAuthUserFromDb = adminAuthUserRepository.findByUsername(adminAuthUser.getUsername())
                    .orElseThrow(() -> new Exception("Admin not found"));
            if(adminAuthUserFromDb.getPassword().equals(adminAuthUser.getPassword())){
                return ResponseEntity.ok(adminAuthUserFromDb);
            }else{
                return ResponseEntity.badRequest().body("Invalid password");
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
}

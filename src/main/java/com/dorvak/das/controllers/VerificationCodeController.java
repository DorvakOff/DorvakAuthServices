package com.dorvak.das.controllers;

import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.auth.CodeGenerator;
import com.dorvak.das.models.User;
import com.dorvak.das.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/verify")
public class VerificationCodeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{code}")
    public ResponseEntity<Void> verify(@PathVariable String code) {
        UUID id = CodeGenerator.verifyCode(code);

        if (id == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setVerified(true);
        userRepository.save(user);

        DorvakAuthServices.getLogger().info("Verified user " + user.getUsername());
        return ResponseEntity.ok().build();
    }
}

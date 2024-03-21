package com.carlinker.controllers;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/addUser")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        try {
            String createUserMsg = adminService.createUser(signupRequest);
            return new ResponseEntity<>(createUserMsg, HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>("You are not authorized to access this resource", HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

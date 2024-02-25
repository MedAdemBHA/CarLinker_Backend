package com.carlinker.controllers;


import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UserDto;
import com.carlinker.services.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    // Constructor is either public or package-private
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        try {
           String createUserMsg= authService.createUser(signupRequest);
            return new ResponseEntity<>(createUserMsg, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}

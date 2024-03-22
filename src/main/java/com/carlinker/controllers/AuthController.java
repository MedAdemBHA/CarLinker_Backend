package com.carlinker.controllers;


import com.carlinker.dtos.AuthenticationRequest;
import com.carlinker.dtos.AuthenticationResponse;
import com.carlinker.dtos.SignupRequest;
import com.carlinker.entities.User;
import com.carlinker.enums.UserRole;
import com.carlinker.repositories.UserRepo;
import com.carlinker.services.auth.AuthService;
import com.carlinker.services.jwt.UserDetailsServiceImpl;
import com.carlinker.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private  final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepo userRepo;
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, UserRepo userRepo) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
    }

    @CrossOrigin(origins = "http://localhost:5173")
        @PostMapping("/signup")
        public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
            try {
               String createUserMsg= authService.createUser(signupRequest);
                return new ResponseEntity<>(createUserMsg, HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    private UserRole getUserRole(String email) {
        Optional<User> optionalUser = userRepo.findFirstByEmail(email);
        return optionalUser.map(User::getUserRole).orElse(null);
    }

    private long getUserId(String email) {
        Optional<User> optionalUser = userRepo.findFirstByEmail(email);
        return optionalUser.map(User::getId).orElse(0L);
    }

    private String getName(String email) {
        Optional<User> optionalUser = userRepo.findFirstByEmail(email);
        return optionalUser.map(User::getName).orElse(null);
    }
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:5173")

    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is disabled: " + disabledException.getMessage());
            return null;
        }
        final UserDetails userDetails =userDetailsService.loadUserByUsername((authenticationRequest.getEmail()));
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), getUserRole(userDetails.getUsername()),getName(userDetails.getUsername()), getUserId(userDetails.getUsername()));
        Optional<User> optionalUser =userRepo.findFirstByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse=new AuthenticationResponse();
        if (optionalUser.isPresent())
        {
            authenticationResponse.setJwt(jwt);
            User user = optionalUser.get();
            user.setLastLoginDate(LocalDateTime.now()); // Update lastLoginDate to current time
            userRepo.save(user);


        }
        return ResponseEntity.ok(authenticationResponse);
    }


}

package com.carlinker.services.auth;

import com.carlinker.dtos.SignupRequest;

import com.carlinker.entities.User;
import com.carlinker.enums.UserRole;
import com.carlinker.repositories.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AuthServiceImplementation implements AuthService {
private final UserRepo userRepo;

    public AuthServiceImplementation(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepo.findByUserRole(UserRole.ADMIN);
        if (adminAccount==null){
            User user = new User();
            user.setName("admin");
            user.setEmail("admin@test.com");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setUserRole(UserRole.ADMIN);
            userRepo.save(user);

        }
    }


    @Override
    public String createUser(SignupRequest signupRequest) {
        List<User> existingUsers = userRepo.findByEmail(signupRequest.getEmail());

        if (!existingUsers.isEmpty()) {
            throw new RuntimeException( "Email already in use. Please choose a different email.");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.USER);

        userRepo.save(user);

        // Return success message
        return "User registered successfully";





    }
}

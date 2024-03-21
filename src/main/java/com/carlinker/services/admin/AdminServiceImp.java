package com.carlinker.services.admin;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.entities.User;
import com.carlinker.enums.UserRole;
import com.carlinker.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService{

    private final UserRepo userRepo;

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

package com.carlinker.services.admin;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.entities.User;
import com.carlinker.enums.UserRole;
import com.carlinker.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Override

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public String updateUser(Long userId, UpdateProfileRequest updateProfileRequest) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(updateProfileRequest.getName());
            user.setEmail(updateProfileRequest.getEmail());
            user.setActive(updateProfileRequest.getIsActive());


            userRepo.save(user);
            return "User updated successfully";
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            userRepo.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    public boolean activateUser(Long userId, boolean activate) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(activate);
            userRepo.save(user);
            return true; // Activation successful
        } else {
            return false; // User not found
        }
    }
    @Override
    public List<User> getActiveUsers() {
        return userRepo.findByIsActive(true);
    }

    @Override
    public List<User> getInactiveUsers() {
        return userRepo.findByIsActive(false);
    }

    public long countAllUsers() {
        return userRepo.count();
    }

    @Override
    public long countActiveUsers() {
        return userRepo.countByIsActive(true);
    }

    @Override
    public long countInactiveUsers() {
        return userRepo.countByIsActive(false);
    }
}

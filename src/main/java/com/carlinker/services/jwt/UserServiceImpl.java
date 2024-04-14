package com.carlinker.services.jwt;

import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.entities.User;
import com.carlinker.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
private final UserRepo userRepo;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findFirstByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not Found "));
            }
        };
    }
    @Override
    public String updateUser(Long userId, UpdateProfileRequest signupRequest) {
        Optional<com.carlinker.entities.User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(signupRequest.getName());
            user.setEmail(signupRequest.getEmail());
            user.setCity(signupRequest.getCity());
            user.setPhone(signupRequest.getPhone());
            if (signupRequest.getPassword() != null && !signupRequest.getPassword().isEmpty()) {
                user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            }
            // Save the updated user
            userRepo.save(user);
            return "User updated successfully";
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    @Override
    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        return optionalUser.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    @Override
    public User getUserByEmail(String email) {
        List<User> users = userRepo.findByEmail(email);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return users.get(0);
    }
    @Override
    public int getNumberOfCarsPostedByUser(Long userId) {
        User user = getUserById(userId);
        return user.getCars().size();
    }




}

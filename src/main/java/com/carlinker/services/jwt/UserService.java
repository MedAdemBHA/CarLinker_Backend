package com.carlinker.services.jwt;

import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();
    User getUserById(Long userId);
    String updateUser(Long userId, UpdateProfileRequest updateProfileRequest);
    User getUserByEmail(String email);
}

package com.carlinker.services.auth;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UserDto;
import org.springframework.stereotype.Service;


public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
}

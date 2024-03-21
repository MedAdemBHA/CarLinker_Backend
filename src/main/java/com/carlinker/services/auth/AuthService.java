package com.carlinker.services.auth;

import com.carlinker.dtos.SignupRequest;


public interface AuthService {
    String createUser(SignupRequest signupRequest);


}

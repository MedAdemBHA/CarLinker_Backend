package com.carlinker.services.admin;

import com.carlinker.dtos.SignupRequest;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {


    String createUser(SignupRequest signupRequest);
}

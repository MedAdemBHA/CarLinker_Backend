package com.carlinker.services.admin;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {


    String createUser(SignupRequest signupRequest);
    String updateUser(Long userId, UpdateProfileRequest updateProfileRequest);

    void deleteUser(Long userId);
    List<User> getAllUsers();
    boolean activateUser(Long userId, boolean activate);


    List<User> getActiveUsers();
    List<User> getInactiveUsers();
    long countAllUsers();
    long countActiveUsers();
    long countInactiveUsers();

}

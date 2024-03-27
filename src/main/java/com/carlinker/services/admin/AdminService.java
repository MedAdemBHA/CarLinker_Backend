package com.carlinker.services.admin;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {


    String createUser(SignupRequest signupRequest);
    String updateUser(Long userId, SignupRequest signupRequest);

    void deleteUser(Long userId);
    List<User> getAllUsers();
    void activateUser(Long userId);

    void deactivateUser(Long userId);
    List<User> getActiveUsers();
    List<User> getInactiveUsers();
    long countAllUsers();
    long countActiveUsers();
    long countInactiveUsers();

}

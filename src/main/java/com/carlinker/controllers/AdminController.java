package com.carlinker.controllers;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UserCountsDTO;
import com.carlinker.entities.User;
import com.carlinker.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        try {
            String createUserMsg = adminService.createUser(signupRequest);
            return new ResponseEntity<>(createUserMsg, HttpStatus.CREATED);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody SignupRequest signupRequest) {
        try {
            String updateUserMsg = adminService.updateUser(userId, signupRequest);
            return new ResponseEntity<>(updateUserMsg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/activateUser/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
    }

    @PutMapping("/deactivateUser/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return new ResponseEntity<>("User deactivated successfully", HttpStatus.OK);
    }
    @GetMapping("/activeUsers")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = adminService.getActiveUsers();
        return new ResponseEntity<>(activeUsers, HttpStatus.OK);
    }

    @GetMapping("/inactiveUsers")
    public ResponseEntity<List<User>> getInactiveUsers() {
        List<User> inactiveUsers = adminService.getInactiveUsers();
        return new ResponseEntity<>(inactiveUsers, HttpStatus.OK);
    }
    @GetMapping("/userCounts")
    public ResponseEntity<UserCountsDTO> getUserCounts() {
        long totalUsers = adminService.countAllUsers();
        long activeUsers = adminService.countActiveUsers();
        long inactiveUsers = adminService.countInactiveUsers();

        UserCountsDTO userCounts = new UserCountsDTO(totalUsers, activeUsers, inactiveUsers);
        return new ResponseEntity<>(userCounts, HttpStatus.OK);
    }
}

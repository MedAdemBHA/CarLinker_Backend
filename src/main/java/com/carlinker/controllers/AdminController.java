package com.carlinker.controllers;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.dtos.UserCountsDTO;
import com.carlinker.dtos.UserDto;
import com.carlinker.entities.User;
import com.carlinker.services.admin.AdminService;
import com.carlinker.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
private final UserService userService;
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> userList = adminService.getAllUsers();
        List<UserDto> userDtoList = userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setCity(user.getCity());
        userDto.setUserRole(user.getUserRole());
        userDto.setIsActive(user.getActive());
        userDto.setLastLoginDate(user.getLastLoginDate());
        return userDto;
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
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            String updateUserMsg = adminService.updateUser(userId, updateProfileRequest);
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




    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setIsActive(user.getActive());
            userDto.setUserRole(user.getUserRole());

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/activateUser/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Long userId, @RequestBody SignupRequest request) {
        boolean activationResult = adminService.activateUser(userId, request.getIsActive());
        if (activationResult) {
            return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User activation failed", HttpStatus.NOT_FOUND);
        }
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

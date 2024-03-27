package com.carlinker.controllers;
import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.dtos.UserDto;
import com.carlinker.entities.User;
import com.carlinker.services.jwt.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateProfileRequest updateProfileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        try {
            String updateUserMsg = userService.updateUser(userId, updateProfileRequest);
            return new ResponseEntity<>(updateUserMsg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        User user = userService.getUserById(userId);

        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setCity(user.getCity());
            userDto.setPhone(user.getPhone());
            userDto.setIsActive(user.getActive());
            userDto.setUserRole(user.getUserRole());
            userDto.setLastLoginDate(user.getLastLoginDate());

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        return user.getId();
    }

}

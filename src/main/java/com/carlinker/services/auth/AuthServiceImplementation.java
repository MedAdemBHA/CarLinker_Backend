package com.carlinker.services.auth;

import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UserDto;
import com.carlinker.entities.User;
import com.carlinker.enums.UserRole;
import com.carlinker.repositories.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AuthServiceImplementation implements AuthService {
private final UserRepo userRepo;

    public AuthServiceImplementation(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        List<User> existingUsers = userRepo.findByEmail(signupRequest.getEmail());

        if (!existingUsers.isEmpty()) {
            throw new RuntimeException("Email already in use. Please choose a different email.");
        }
        User user =new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.USER);
        User createUser =userRepo.save(user);
        UserDto createUserDto = new UserDto();
        createUserDto.setId(createUser.getId());
        createUserDto.setEmail(createUser.getEmail());
        createUserDto.setName(createUser.getName());
        createUserDto.setUserRole(createUser.getUserRole());
        createUserDto.setPhone(createUser.getPhone());
        createUserDto.setLastLoginDate(createUser.getLastLoginDate());
        createUserDto.setCity(createUser.getCity());
        createUserDto.setActive(createUser.getActive());


        return createUserDto;





    }
}

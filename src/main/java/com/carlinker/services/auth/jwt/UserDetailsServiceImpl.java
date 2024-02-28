package com.carlinker.services.auth.jwt;

import com.carlinker.entities.User;
import com.carlinker.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // write logic to get user from db
        Optional<User> optionalUser= userRepo.findFirstByEmail(email);
      if (optionalUser.isEmpty()) throw new UsernameNotFoundException("User not found",null);
      return new org.springframework.security.core.userdetails.User(
              optionalUser.get().getEmail(),
              optionalUser.get().getPassword(),
              new ArrayList<>());



    }
}

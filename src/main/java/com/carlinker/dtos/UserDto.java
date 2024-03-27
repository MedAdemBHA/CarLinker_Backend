package com.carlinker.dtos;

import com.carlinker.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor

public class UserDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String city;
    private Boolean isActive;
    private UserRole userRole;
    private LocalDateTime lastLoginDate;

}

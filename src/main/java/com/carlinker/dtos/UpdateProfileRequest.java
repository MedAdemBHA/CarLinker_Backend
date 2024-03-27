package com.carlinker.dtos;

import com.carlinker.enums.UserRole;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private String city;
    private String email;
    private String password;
    private UserRole userRole;
    private Boolean isActive;



}

package com.carlinker.dtos;

import com.carlinker.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse{
    private String jwt;


}

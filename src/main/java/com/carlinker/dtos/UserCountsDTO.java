package com.carlinker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCountsDTO {

        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;


}

package com.carlinker.dtos;

public class UserCountsDTO {

        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;

        public UserCountsDTO(long totalUsers, long activeUsers, long inactiveUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.inactiveUsers = inactiveUsers;
        }

        // Getters and setters
        public long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public long getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(long activeUsers) {
            this.activeUsers = activeUsers;
        }

        public long getInactiveUsers() {
            return inactiveUsers;
        }

        public void setInactiveUsers(long inactiveUsers) {
            this.inactiveUsers = inactiveUsers;
        }
}

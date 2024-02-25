package com.carlinker.entities;

import com.carlinker.enums.UserRole;
import org.springframework.lang.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 255)
    private String name;
    @Column(length = 255, unique = true)
    private String email;
    private String password;
    private UserRole userRole;
    @Nullable
    private String phone;
    @Nullable
    private String city;
    @Nullable
    private Boolean isActive;

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(@Nullable Boolean active) {
        isActive = active;
    }

    @Nullable
    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(@Nullable LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Nullable
    private LocalDateTime lastLoginDate;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}

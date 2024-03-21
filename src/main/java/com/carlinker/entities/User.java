package com.carlinker.entities;

import com.carlinker.enums.UserRole;
import org.springframework.lang.Nullable;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity

@Table(name="users")
public class User implements UserDetails {
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Announcement> announcements = new ArrayList<>();

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(UserRole.USER.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
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

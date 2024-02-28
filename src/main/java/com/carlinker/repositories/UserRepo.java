package com.carlinker.repositories;

import com.carlinker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


// Annotation indicating that this interface is a Spring Data repository
@Repository
public interface UserRepo  extends JpaRepository <User,Long>{

    // Custom query method to find a list of users by their email
    // Follows the Spring Data JPA naming convention
    List<User> findByEmail(String email);
    Optional<User> findFirstByEmail(String email);


}

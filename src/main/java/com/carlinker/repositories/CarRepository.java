package com.carlinker.repositories;

import com.carlinker.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByUserId(Long userId);
    List<Car> findByStatus(boolean status);
}




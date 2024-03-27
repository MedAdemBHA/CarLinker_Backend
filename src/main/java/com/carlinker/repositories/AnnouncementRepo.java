package com.carlinker.repositories;

import com.carlinker.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepo extends JpaRepository<Car,Long> {



}

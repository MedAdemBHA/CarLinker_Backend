package com.carlinker.repositories;

import com.carlinker.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepo extends JpaRepository<Announcement,Long> {



}

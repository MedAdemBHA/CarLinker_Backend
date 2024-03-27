package com.carlinker.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@Table(name ="Announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String color;
    private String fuelType;
    private long price;
    private String year;
    private boolean approved;

    private String description;
    private String location;
@Column(columnDefinition = "longblob")
    private byte[] image;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;





}

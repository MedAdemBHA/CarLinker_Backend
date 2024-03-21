package com.carlinker.dtos;

import com.carlinker.entities.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AnnouncementDto {
    private long id;
    private String brand;
    private String color;
    private String fuelType;
    private long price;
    private String year;
    private boolean approved;

    private String description;
    private String location;

    private byte[] image;
    private MultipartFile returnedImage;
    private User user;

}

package com.carlinker.dtos;

import com.carlinker.enums.OptionType;
import com.carlinker.enums.TransmissionType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarDto {
    private long id;
    private String manufacturer;
    private String model;
    private String mileage;
    private String color;
    private String fuelType;
    private Long price;
    private String year;
    private Boolean status;
    private List<OptionType> option;
    private TransmissionType transmission;
    private String description;
    private String location;
    private List<MultipartFile> images;
    private List<String> imageFiles;
    private long userID;
    private String name;
    private String phone;
    private LocalDateTime lastLoginDate;

}

package com.carlinker.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name ="Car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changed primitive type to wrapper type

    private String manufacturer;
    private String model;
    private String mileage;
    private String color;
    private String fuelType;
    private Long price;
    private String year;
    private Boolean status;
    private String option;
    private String transmission;

    private String description;
    private String location;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

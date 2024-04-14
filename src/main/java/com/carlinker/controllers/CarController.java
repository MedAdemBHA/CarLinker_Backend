package com.carlinker.controllers;

import com.carlinker.dtos.CarDto;
import com.carlinker.entities.Car;
import com.carlinker.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarRepository carRepository;

    @GetMapping("/active")
    public ResponseEntity<List<CarDto>> getActiveCars() {
        try {
            List<Car> cars = carRepository.findByStatus(true);
            List<CarDto> carDtos = new ArrayList<>();

            for (Car car : cars) {
                CarDto carDto = new CarDto();
                BeanUtils.copyProperties(car, carDto);
                // Manually copy each field from Car entity to CarDto
                carDto.setId(car.getId());
                carDto.setManufacturer(car.getManufacturer());
                carDto.setModel(car.getModel());
                carDto.setMileage(car.getMileage());
                carDto.setColor(car.getColor());
                carDto.setFuelType(car.getFuelType());
                carDto.setPrice(car.getPrice());
                carDto.setYear(car.getYear());
                carDto.setStatus(car.getStatus());
                carDto.setOption(car.getOption());
                carDto.setTransmission(car.getTransmission());
                carDto.setDescription(car.getDescription());
                carDto.setLocation(car.getLocation());
                carDto.setUserID(car.getUser().getId());
                carDto.setName(car.getUser().getUsername());
                carDto.setPhone(car.getUser().getPhone());
                carDto.setLastLoginDate(car.getUser().getLastLoginDate());
                List<String> imagePaths = car.getImageFiles().stream()
                        .map(fileName -> "/images/" + fileName)
                        .collect(Collectors.toList());

                carDto.setImageFiles(imagePaths);
                carDtos.add(carDto);
            }

            return ResponseEntity.ok(carDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

package com.carlinker.controllers;
import com.carlinker.dtos.CarDto;
import com.carlinker.dtos.SignupRequest;
import com.carlinker.dtos.UpdateProfileRequest;
import com.carlinker.dtos.UserDto;
import com.carlinker.entities.Car;
import com.carlinker.entities.User;
import com.carlinker.enums.OptionType;
import com.carlinker.repositories.CarRepository;
import com.carlinker.services.car.CarService;
import com.carlinker.services.jwt.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final CarRepository carRepository;
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateProfileRequest updateProfileRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        try {
            String updateUserMsg = userService.updateUser(userId, updateProfileRequest);
            return new ResponseEntity<>(updateUserMsg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        User user = userService.getUserById(userId);

        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setCity(user.getCity());
            userDto.setPhone(user.getPhone());
            userDto.setIsActive(user.getActive());
            userDto.setUserRole(user.getUserRole());
            userDto.setLastLoginDate(user.getLastLoginDate());

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        return user.getId();
    }
    @PostMapping("/cars")
    public ResponseEntity<String> addCar(@RequestParam("images") MultipartFile[] images, @ModelAttribute CarDto carDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        User user = userService.getUserById(userId);
        if (user == null || !user.getActive()) {
            return new ResponseEntity<>("Only active users can post cars", HttpStatus.BAD_REQUEST);
        }
        if (user.getCars().size() >= 3) {
            return new ResponseEntity<>("You have already posted the maximum allowed number of cars", HttpStatus.BAD_REQUEST);
        }
        try {
            Car car = new Car();
            car.setManufacturer(carDto.getManufacturer());
            car.setUser(user);
            car.setModel(carDto.getModel());
            car.setMileage(carDto.getMileage());
            car.setColor(carDto.getColor());
            car.setFuelType(carDto.getFuelType());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setStatus(false);
            car.setOption(carDto.getOption());

            car.setTransmission(carDto.getTransmission());
            car.setDescription(carDto.getDescription());
            car.setLocation(carDto.getLocation());
            // Save the images
            List<String> imageFiles = new ArrayList<>();
            for (MultipartFile image : images) {
                imageFiles.add(saveImage(image));
            }
            car.setImageFiles(imageFiles);

            // Save the new Car entity to the database
            carRepository.save(car);

            return new ResponseEntity<>("Car added successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add car", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/cars/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @ModelAttribute CarDto carDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            List<Car> userCars = carRepository.findByUserId(userId);
            Optional<Car> optionalCar = userCars.stream()
                    .filter(car -> car.getId().equals(id))
                    .findFirst();

            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>("Car not found or does not belong to you", HttpStatus.NOT_FOUND);
            }

            Car car = optionalCar.get();
            car.setManufacturer(carDto.getManufacturer());
            car.setModel(carDto.getModel());
            car.setMileage(carDto.getMileage());
            car.setColor(carDto.getColor());
            car.setFuelType(carDto.getFuelType());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setOption(carDto.getOption());
            car.setTransmission(carDto.getTransmission());
            car.setDescription(carDto.getDescription());
            car.setLocation(carDto.getLocation());

            // Update image files if new images are provided
            if (carDto.getImages() != null ) {
                List<String> imageFiles = new ArrayList<>();
                for (MultipartFile image : carDto.getImages()) {
                    imageFiles.add(saveImage(image));
                }
                car.setImageFiles(imageFiles);
            }

            carRepository.save(car);

            return new ResponseEntity<>("Car updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update car", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return storageFileName; // Return the filename only
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            throw ex; // Re-throw the exception to handle it at the higher level
        }
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id) {
        try {
            Optional<Car> optionalCar = carRepository.findById(id);
            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Car car = optionalCar.get();
            CarDto carDto = new CarDto();
            BeanUtils.copyProperties(car, carDto);
            carDto.setUserID(car.getUser().getId());

            // Convert image file names to image paths
            List<String> imagePaths = car.getImageFiles().stream()
                    .map(fileName -> "/images/" + fileName)
                    .collect(Collectors.toList());

            // Set the list of image paths to the imageFiles field in the CarDto
            carDto.setImageFiles(imagePaths);

            return new ResponseEntity<>(carDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/mycars")
    public ResponseEntity<List<CarDto>> getUserCars() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);
        try {
            List<Car> userCars = carRepository.findByUserId(userId);
            if (userCars.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<CarDto> userCarDtos = new ArrayList<>();
            for (Car car : userCars) {
                CarDto carDto = new CarDto();
                BeanUtils.copyProperties(car, carDto);
                carDto.setUserID(userId);

                carDto.setUserID(car.getUser().getId());
                carDto.setName(car.getUser().getUsername());
                carDto.setPhone(car.getUser().getPhone());
                carDto.setLastLoginDate(car.getUser().getLastLoginDate());
                List<String> imagePaths = car.getImageFiles().stream()
                        .map(fileName -> "/images/" + fileName)
                        .collect(Collectors.toList());

                // Set the list of image paths to the imageFiles field in the CarDto
                carDto.setImageFiles(imagePaths);

                userCarDtos.add(carDto);
            }

            return new ResponseEntity<>(userCarDtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/mycars/{carId}")
    public ResponseEntity<String> deleteUserCarById(@PathVariable Long carId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(authentication);

        try {
            Optional<Car> optionalCar = carRepository.findById(carId);
            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }

            Car car = optionalCar.get();
            if (car.getUser().getId() != userId) {
                return new ResponseEntity<>("You are not authorized to delete this car", HttpStatus.UNAUTHORIZED);
            }

            carRepository.delete(car);
            return new ResponseEntity<>("Car deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete car", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

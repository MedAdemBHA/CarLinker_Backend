package com.carlinker.controllers;

import com.carlinker.dtos.*;
import com.carlinker.entities.Car;
import com.carlinker.entities.User;
import com.carlinker.repositories.CarRepository;
import com.carlinker.services.admin.AdminService;
import com.carlinker.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final CarRepository carRepository;
private final UserService userService;
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> userList = adminService.getAllUsers();
        List<UserDto> userDtoList = userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setCity(user.getCity());
        userDto.setUserRole(user.getUserRole());
        userDto.setIsActive(user.getActive());
        userDto.setLastLoginDate(user.getLastLoginDate());
        return userDto;
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        try {
            String createUserMsg = adminService.createUser(signupRequest);
            return new ResponseEntity<>(createUserMsg, HttpStatus.CREATED);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            String updateUserMsg = adminService.updateUser(userId, updateProfileRequest);
            return new ResponseEntity<>(updateUserMsg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setIsActive(user.getActive());
            userDto.setUserRole(user.getUserRole());

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/activateUser/{userId}")
    public ResponseEntity<String> activateUser(@PathVariable Long userId, @RequestBody SignupRequest request) {
        boolean activationResult = adminService.activateUser(userId, request.getIsActive());
        if (activationResult) {
            return new ResponseEntity<>("User activated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User activation failed", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/activeUsers")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = adminService.getActiveUsers();
        return new ResponseEntity<>(activeUsers, HttpStatus.OK);
    }

    @GetMapping("/inactiveUsers")
    public ResponseEntity<List<User>> getInactiveUsers() {
        List<User> inactiveUsers = adminService.getInactiveUsers();
        return new ResponseEntity<>(inactiveUsers, HttpStatus.OK);
    }
    @GetMapping("/userCounts")
    public ResponseEntity<UserCountsDTO> getUserCounts() {
        long totalUsers = adminService.countAllUsers();
        long activeUsers = adminService.countActiveUsers();
        long inactiveUsers = adminService.countInactiveUsers();

        UserCountsDTO userCounts = new UserCountsDTO(totalUsers, activeUsers, inactiveUsers);
        return new ResponseEntity<>(userCounts, HttpStatus.OK);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getCars() {
        try {
            List<Car> cars = carRepository.findAll();
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
    @GetMapping("/cars/inactive")
    public ResponseEntity<List<CarDto>> getInactiveCars() {
        try {
            List<Car> cars = carRepository.findByStatus(false); // Assuming you have a method in the repository to fetch cars by status
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
    @PutMapping("/cars/{id}/activate")
    public ResponseEntity<String> toggleActivateCar(@PathVariable Long id) {
        try {
            Optional<Car> optionalCar = carRepository.findById(id);
            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }

            Car car = optionalCar.get();
            boolean currentStatus = car.getStatus();
            car.setStatus(!currentStatus);

            carRepository.save(car);

            String message = currentStatus ? "Car deactivated successfully" : "Car activated successfully";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to toggle car activation status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        try {
            Optional<Car> optionalCar = carRepository.findById(id);
            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }

            carRepository.deleteById(id);
            return new ResponseEntity<>("Car deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete car", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/cars/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @ModelAttribute CarDto carDto) {
        try {
            Optional<Car> optionalCar = carRepository.findById(id);
            if (!optionalCar.isPresent()) {
                return new ResponseEntity<>("Car not found", HttpStatus.NOT_FOUND);
            }

            Car car = optionalCar.get();
            car.setManufacturer(carDto.getManufacturer());
            car.setModel(carDto.getModel());
            car.setMileage(carDto.getMileage());
            car.setColor(carDto.getColor());
            car.setFuelType(carDto.getFuelType());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setStatus(carDto.getStatus());
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


}

package com.carlinker.services.car;

import com.carlinker.repositories.CarRepository;
import com.carlinker.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final UserRepo userRepo ;





}

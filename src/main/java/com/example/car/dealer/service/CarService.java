package com.example.car.dealer.service;

import com.example.car.dealer.entity.Car;
import com.example.car.dealer.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Fetch sorted cars based on status and order type


    public List<String> getAllCarMakes() {
        return carRepository.findAll().stream()
                .map(Car::getBrand)
                .distinct()
                .collect(Collectors.toList());
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }


}

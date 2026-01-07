package com.example.car.dealer.service;

import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    // ✅ dropdown makes
    public List<String> getAllCarMakes() {
        return carRepository.findDistinctBrands();
    }

    // ✅ dropdown models by make
    public List<String> getModelsByMake(String make) {
        return carRepository.findDistinctModelsByMake(make);
    }

    // ✅ NEW / USED only
    public List<Car> getCarsByStatus(String status) {
        if (status == null || status.isBlank()) {
            return carRepository.findAll();
        }

        CarStatus carStatus;
        try {
            carStatus = CarStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of(); // invalid NEW/USED
        }

        return carRepository.findByStatus(carStatus);
    }

    // ✅ status + make + model + order
    public List<Car> filterCars(String status, String make, String model,
                                Double minPrice, Double maxPrice,
                                String order) {

        CarStatus carStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                carStatus = CarStatus.valueOf(status.trim().toUpperCase()); // NEW / USED
            } catch (IllegalArgumentException e) {
                return List.of();
            }
        }

        if ("desc".equalsIgnoreCase(order)) {
            return carRepository.filterDesc(carStatus, make, model, minPrice, maxPrice);
        }
        return carRepository.filterAsc(carStatus, make, model, minPrice, maxPrice);
    }

}

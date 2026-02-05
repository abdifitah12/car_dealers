package com.example.car.dealer.service;
import com.example.car.dealer.repository.CarRepository;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // ✅ Only AVAILABLE cars should show on website
    public List<Car> getAllCars() {
        return carRepository.findByAvailability(CarAvailability.AVAILABLE);
    }

    // ✅ When adding a car, make it AVAILABLE by default
    public Car addCar(Car car) {
        if (car.getAvailability() == null) {
            car.setAvailability(CarAvailability.AVAILABLE);
        }
        return carRepository.save(car);
    }

    // ✅ Mark car as SOLD (hide from website)
    public void markAsSold(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        car.setAvailability(CarAvailability.SOLD);
        carRepository.save(car);
    }

    // ✅ dropdown makes (only available cars)
    public List<String> getAllCarMakes() {
        return carRepository.findDistinctBrandsAvailable();
    }

    // ✅ dropdown models by make (only available cars)
    public List<String> getModelsByMake(String make) {
        return carRepository.findDistinctModelsByMakeAvailable(make);
    }

    // ✅ NEW / USED only (but only AVAILABLE cars)
    public List<Car> getCarsByStatus(String status) {
        if (status == null || status.isBlank()) {
            return carRepository.findByAvailability(CarAvailability.AVAILABLE);
        }

        CarStatus carStatus;
        try {
            carStatus = CarStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of();
        }

        return carRepository.findByStatusAndAvailability(carStatus, CarAvailability.AVAILABLE);
    }

    // ✅ status + make + model + order (ONLY AVAILABLE cars)
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
            return carRepository.filterDescAvailable(carStatus, make, model, minPrice, maxPrice);
        }
        return carRepository.filterAscAvailable(carStatus, make, model, minPrice, maxPrice);
    }
}

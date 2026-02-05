package com.example.car.dealer.service;

import com.example.car.dealer.dto.CarResponse;
import com.example.car.dealer.entity.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarResponseService {

    public CarResponse toDto(Car car) {
        CarResponse dto = new CarResponse();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setPrice(car.getPrice());
        dto.setStatus(car.getStatus() == null ? null : car.getStatus().name());
        dto.setAvailability(car.getAvailability() == null ? null : car.getAvailability().name());
        dto.setYear(car.getYear());
        dto.setImagesBase64(car.getImagesBase64());
        return dto;
    }

    public List<CarResponse> toDtoList(List<Car> cars) {
        return cars.stream().map(this::toDto).toList();
    }
}

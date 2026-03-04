package com.example.car.dealer.service;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.CarImage;
import com.example.car.dealer.repository.AdminCarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdminCarService {

    private final AdminCarRepository adminCarRepository;

    public AdminCarService(AdminCarRepository adminCarRepository) {
        this.adminCarRepository = adminCarRepository;
    }

    public List<Car> getAllCars() {
        return adminCarRepository.findAll();
    }

    public Car getCarById(Long id) {
        return adminCarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + id));
    }

    public List<Car> getFinanceCars() {
        return adminCarRepository.findByAvailability(CarAvailability.FINANCE);
    }

    @Transactional
    public Car createCar(Car car, List<MultipartFile> imageFiles) {
        // attach images if uploaded
        if (hasNewImages(imageFiles)) {
            car.getImages().clear();
            addImagesToCar(car, imageFiles);
        }
        return adminCarRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, Car updated, List<MultipartFile> imageFiles) {
        Car existing = getCarById(id);

        // ✅ update fields
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setPrice(updated.getPrice());
        existing.setYear(updated.getYear());
        existing.setStatus(updated.getStatus());
        existing.setAvailability(updated.getAvailability());


        // ✅ If new images uploaded -> replace old images
        if (hasNewImages(imageFiles)) {
            // Because orphanRemoval=true, clearing removes old images rows
            existing.getImages().clear();
            addImagesToCar(existing, imageFiles);
        }

        return adminCarRepository.save(existing);
    }

    @Transactional
    public void deleteCar(Long id) {
        if (!adminCarRepository.existsById(id)) {
            throw new IllegalArgumentException("Car not found with id: " + id);
        }
        markAsSold(id);
    }
    
    @Transactional
    public void markAsSold(Long id) {
        Car car = getCarById(id);
        car.setAvailability(CarAvailability.SOLD);
        adminCarRepository.save(car);
    }


    // ---------------- helpers ----------------

    private boolean hasNewImages(List<MultipartFile> files) {
        return files != null && files.stream().anyMatch(f -> f != null && !f.isEmpty());
    }

    private void addImagesToCar(Car car, List<MultipartFile> files) {
        if (files == null) return;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            CarImage img = new CarImage();
            img.setCar(car);

            try {
                img.setImageData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read image: " + file.getOriginalFilename(), e);
            }

            car.getImages().add(img);
        }
    }
}

package com.example.car.dealer.entity;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.carEnum.CarStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Data
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;


    @Column(length = 30)
    private String transmission;      // Automatic / Manual

    @Column(length = 30)
    private String fuelType;          // Gas / Hybrid / Electric



    @Column(length = 120)
    private String location;

    // ✅ Images table relation
    @OneToMany(mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<CarImage> images = new ArrayList<>();

    @Column(nullable = false)
    private Double price;

    // ✅ NEW / USED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    // ✅ AVAILABLE / SOLD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarAvailability availability = CarAvailability.AVAILABLE;

    @Column(nullable = false)
    private Integer year;

    // ✅ Used by Thymeleaf + DTO
    @Transient
    public List<String> getImagesBase64() {
        List<String> base64Images = new ArrayList<>();
        for (CarImage img : images) {
            if (img != null && img.getImageData() != null) {
                base64Images.add(Base64.getEncoder().encodeToString(img.getImageData()));
            }
        }
        return base64Images;
    }
}

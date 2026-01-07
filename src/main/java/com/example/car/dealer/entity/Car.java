package com.example.car.dealer.entity;

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

    // ✅ Correct relationship for table car_images(id, car_id, image_data)
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CarImage> images = new ArrayList<>();

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

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

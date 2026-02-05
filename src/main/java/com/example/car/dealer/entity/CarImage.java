package com.example.car.dealer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "car_images")
@Getter
@Setter
@NoArgsConstructor
public class CarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Store image bytes in DB
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_data", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;


    // ✅ Many images belong to one car
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // ✅ Optional: store filename (helps for debugging)
    @Column(name = "file_name", length = 255)
    private String fileName;

    // ✅ Optional: store content type (image/jpeg, image/png, ...)
    @Column(name = "content_type", length = 100)
    private String contentType;

    public CarImage(Car car, byte[] imageData) {
        this.car = car;
        this.imageData = imageData;
    }

    public CarImage(Car car, byte[] imageData, String fileName, String contentType) {
        this.car = car;
        this.imageData = imageData;
        this.fileName = fileName;
        this.contentType = contentType;
    }
}

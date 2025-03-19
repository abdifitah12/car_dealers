package com.example.car.dealer.entity;

import com.example.car.dealer.carEnum.CarStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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

    @ElementCollection
    @CollectionTable(name = "car_images", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private List<byte[]> images = new ArrayList<>();  // Store multiple images as BLOBs

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Column(nullable = false)
    private Integer year;

    public List<String> getImagesBase64() {
        List<String> base64Images = new ArrayList<>();
        for (byte[] image : images) {
            base64Images.add(java.util.Base64.getEncoder().encodeToString(image));
        }
        return base64Images;
    }

    public Car() {
    }

    public Car(Long id, String brand, String model, List<byte[]> images, Double price, CarStatus status, Integer year) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.images = images;
        this.price = price;
        this.status = status;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}

package com.example.car.dealer.dto;

import lombok.Data;
import java.util.List;

@Data
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private Double price;
    private String status;
    private Integer year;
    private String availability;
    private List<String> imagesBase64; // ✅ frontend uses this
}

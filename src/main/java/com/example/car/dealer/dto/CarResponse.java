package com.example.car.dealer.dto;

import com.example.car.dealer.entity.CarFinance;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
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

    // ✅ New fields
    private String transmission;
    private String fuelType;
    private String location;




    private List<String> imagesBase64; // ✅ frontend uses this

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CarFinance finance;

}

package com.example.car.dealer.repository;

import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrand(String brand);

    // Fetch cars by status and order by price (ascending)
    List<Car> findByStatusOrderByPriceAsc(String status);

    // Fetch cars by status and order by price (descending)
    List<Car> findByStatusOrderByPriceDesc(String status);
}

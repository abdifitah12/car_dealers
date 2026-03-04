package com.example.car.dealer.repository;

import com.example.car.dealer.entity.CarFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarFinanceRepository extends JpaRepository<CarFinance, Long> {
    Optional<CarFinance> findByCarId(Long carId);
}

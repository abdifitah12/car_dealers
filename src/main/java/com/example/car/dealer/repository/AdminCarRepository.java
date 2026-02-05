package com.example.car.dealer.repository;

import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCarRepository extends JpaRepository<Car, Long> {
}

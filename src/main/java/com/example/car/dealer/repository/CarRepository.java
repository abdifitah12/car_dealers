package com.example.car.dealer.repository;

import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    @Query("SELECT c FROM Car c WHERE " +
            "(:status IS NULL OR c.status = :status) " +  // Status filter (optional)
            "AND (:make IS NULL OR c.brand = :make) " +   // Make filter (optional)
            "AND (:model IS NULL OR c.model = :model) " + // Model filter (optional)
            "ORDER BY " +
            "CASE WHEN :order = 'desc' THEN c.price END DESC, " +
            "CASE WHEN :order = 'asc' THEN c.price END ASC")
    List<Car> findByFilters(
            @Param("status") CarStatus status,
            @Param("make") String make,
            @Param("model") String model,
            @Param("order") String order
    );



}

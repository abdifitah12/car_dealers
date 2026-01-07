package com.example.car.dealer.repository;

import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    // ✅ FILTER ASC (with price)
    @Query("SELECT c FROM Car c " +
            "WHERE (:status IS NULL OR c.status = :status) " +
            "AND (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "AND (:model IS NULL OR :model = '' OR LOWER(c.model) = LOWER(:model)) " +
            "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
            "ORDER BY c.price ASC")
    List<Car> filterAsc(@Param("status") CarStatus status,
                        @Param("make") String make,
                        @Param("model") String model,
                        @Param("minPrice") Double minPrice,
                        @Param("maxPrice") Double maxPrice);

    // ✅ FILTER DESC (with price)
    @Query("SELECT c FROM Car c " +
            "WHERE (:status IS NULL OR c.status = :status) " +
            "AND (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "AND (:model IS NULL OR :model = '' OR LOWER(c.model) = LOWER(:model)) " +
            "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
            "ORDER BY c.price DESC")
    List<Car> filterDesc(@Param("status") CarStatus status,
                         @Param("make") String make,
                         @Param("model") String model,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice);

    // ✅ Dropdown makes
    @Query("SELECT DISTINCT c.brand FROM Car c ORDER BY c.brand")
    List<String> findDistinctBrands();

    // ✅ Dropdown models by make
    @Query("SELECT DISTINCT c.model FROM Car c " +
            "WHERE (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "ORDER BY c.model")
    List<String> findDistinctModelsByMake(@Param("make") String make);

    // ✅ NEW / USED filter
    List<Car> findByStatus(CarStatus status);
}

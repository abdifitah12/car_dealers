package com.example.car.dealer.repository;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    // ✅ Dropdown makes (ONLY AVAILABLE)
    @Query("SELECT DISTINCT c.brand FROM Car c " +
            "WHERE c.availability = com.example.car.dealer.carEnum.CarAvailability.AVAILABLE " +
            "ORDER BY c.brand")
    List<String> findDistinctBrandsAvailable();

    // ✅ Dropdown models by make (ONLY AVAILABLE)
    @Query("SELECT DISTINCT c.model FROM Car c " +
            "WHERE c.availability = com.example.car.dealer.carEnum.CarAvailability.AVAILABLE " +
            "AND (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "ORDER BY c.model")
    List<String> findDistinctModelsByMakeAvailable(@Param("make") String make);

    // ✅ Home page: only available cars
    List<Car> findByAvailability(CarAvailability availability);

    // ✅ NEW/USED + AVAILABLE
    List<Car> findByStatusAndAvailability(CarStatus status, CarAvailability availability);

    // ✅ Filter ASC (ONLY AVAILABLE)
    @Query("SELECT c FROM Car c " +
            "WHERE c.availability = com.example.car.dealer.carEnum.CarAvailability.AVAILABLE " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "AND (:model IS NULL OR :model = '' OR LOWER(c.model) = LOWER(:model)) " +
            "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
            "ORDER BY c.price ASC")
    List<Car> filterAscAvailable(@Param("status") CarStatus status,
                                 @Param("make") String make,
                                 @Param("model") String model,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice);

    // ✅ Filter DESC (ONLY AVAILABLE)
    @Query("SELECT c FROM Car c " +
            "WHERE c.availability = com.example.car.dealer.carEnum.CarAvailability.AVAILABLE " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:make IS NULL OR :make = '' OR LOWER(c.brand) = LOWER(:make)) " +
            "AND (:model IS NULL OR :model = '' OR LOWER(c.model) = LOWER(:model)) " +
            "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
            "ORDER BY c.price DESC")
    List<Car> filterDescAvailable(@Param("status") CarStatus status,
                                  @Param("make") String make,
                                  @Param("model") String model,
                                  @Param("minPrice") Double minPrice,
                                  @Param("maxPrice") Double maxPrice);
}

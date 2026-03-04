package com.example.car.dealer.repository;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminCarRepository extends JpaRepository<Car, Long> {
    @Query("""
   select c from Car c
   left join fetch c.finance f
   where c.availability = com.example.car.dealer.carEnum.CarAvailability.FINANCE
""")
    List<Car> findFinanceCarsWithFinance();

    List<Car> findByAvailability(CarAvailability carAvailability);
}

package com.example.car.dealer.repository;

import com.example.car.dealer.entity.FinancePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancePersonRepository extends JpaRepository<FinancePerson, Long> {}


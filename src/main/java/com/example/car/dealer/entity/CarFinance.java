package com.example.car.dealer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "car_finance")
public class CarFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private FinancePerson person;

    // Finance details
    @Column(nullable = false)
    private Integer termMonths;

    @Column(nullable = false)
    private Double downPayment;

    @Column(nullable = false)
    private Double apr;

    @Column(nullable = false)
    private Double monthlyPayment;

    @Column(nullable = false)
    private Double totalPaid;

    @Column(nullable = false)
    private Double balanceOwed;

    // how many months missed
    @Column(nullable = false)
    private Integer missedMonths = 0;

    // ✅ missed amount = monthlyPayment * missedMonths
    @Column(nullable = false)
    private Double missedAmount = 0.0;

    // ✅ total due now = balanceOwed + missedAmount
    @Column(nullable = false)
    private Double totalDueNow = 0.0;
}

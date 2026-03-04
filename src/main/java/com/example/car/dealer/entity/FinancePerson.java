package com.example.car.dealer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "finance_person")
public class FinancePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=80)
    private String fullName;

    @Column(nullable=false, length=30)
    private String phone;

    @Column(nullable=false, length=120)
    private String email;

    @Column(length=200)
    private String address;
}

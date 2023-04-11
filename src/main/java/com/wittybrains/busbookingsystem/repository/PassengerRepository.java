package com.wittybrains.busbookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wittybrains.busbookingsystem.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
}

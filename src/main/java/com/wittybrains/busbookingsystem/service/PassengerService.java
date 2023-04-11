package com.wittybrains.busbookingsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import org.springframework.stereotype.Service;


import com.wittybrains.busbookingsystem.dto.PassengerDTO;
import com.wittybrains.busbookingsystem.exception.ResourceNotFoundException;
import com.wittybrains.busbookingsystem.model.Passenger;
import com.wittybrains.busbookingsystem.repository.PassengerRepository;

import java.util.logging.Logger;
@Service
public class PassengerService {

    private static final Logger LOGGER = Logger.getLogger(PassengerService.class.getName());

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger createPassenger(@Valid PassengerDTO passengerDTO) {

        // Validate email format
        if (!passengerDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            LOGGER.warning("Invalid email format: " + passengerDTO.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate contact number format
        if (!passengerDTO.getContactNumber().matches("\\d{10}")) {
            LOGGER.warning("Invalid contact number format: " + passengerDTO.getContactNumber());
            throw new IllegalArgumentException("Invalid contact number format");
        }

        // Validate password length
        if (passengerDTO.getPassword().length() < 6) {
            LOGGER.warning("Password is too short: " + passengerDTO.getPassword());
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        Passenger passenger = new Passenger();
        passenger.setName(passengerDTO.getName());

        // Validate that contact number and email are not null
        if (passengerDTO.getContactNumber() != null) {
            passenger.setContactNumber(passengerDTO.getContactNumber());
        } else {
            LOGGER.warning("Contact number is null");
            throw new IllegalArgumentException("Contact number cannot be null");
        }

        if (passengerDTO.getEmail() != null) {
            passenger.setEmail(passengerDTO.getEmail());
        } else {
            LOGGER.warning("Email is null");
            throw new IllegalArgumentException("Email cannot be null");
        }

        // Validate that password is not null
        if (passengerDTO.getPassword() != null) {
            passenger.setPassword(passengerDTO.getPassword());
        } else {
            LOGGER.warning("Password is null");
            throw new IllegalArgumentException("Password cannot be null");
        }

        passenger.setCreatedOn(LocalDateTime.now());
        passenger.setUpdatedOn(LocalDateTime.now());

        Passenger savedPassenger = passengerRepository.save(passenger);
        LOGGER.info("Passenger created: " + savedPassenger);
        return savedPassenger;
    }

    public Optional<Passenger> getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId);
    }



    
    public Passenger updatePassenger(Long passengerId, PassengerDTO passengerDTO) {
        LOGGER.info("Updating passenger with id: {}");
        Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            if (passengerDTO.getName() != null) {
                passenger.setName(passengerDTO.getName());
            }
            if (passengerDTO.getContactNumber() != null && passengerDTO.getContactNumber().matches("[0-9]{10}")) {
                passenger.setContactNumber(passengerDTO.getContactNumber());
            }
            if (passengerDTO.getEmail() != null && passengerDTO.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                passenger.setEmail(passengerDTO.getEmail());
            }
            if (passengerDTO.getPassword() != null && passengerDTO.getPassword().length() > 6) {
                passenger.setPassword(passengerDTO.getPassword());
            }
            passenger.setUpdatedOn(LocalDateTime.now());
            return passengerRepository.save(passenger);
        } else {
            throw new ResourceNotFoundException("Passenger not found with id " + passengerId);
        }
    }



    public void deletePassenger(Long passengerId) {
        passengerRepository.deleteById(passengerId);
    }
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

   
    }

    



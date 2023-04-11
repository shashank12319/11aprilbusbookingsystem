package com.wittybrains.busbookingsystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.dto.DriverDTO;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DriverService {

    private final Logger logger = LoggerFactory.getLogger(DriverService.class);

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(DriverDTO driverDTO) {
        logger.debug("Creating driver with name {} and license number {}", driverDTO.getName(), driverDTO.getLicenseNumber());
        Driver driver = new Driver();
        String name = driverDTO.getName();
        String licenseNumber = driverDTO.getLicenseNumber();

        // Validate name
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        driver.setName(name);

        // Validate license number
        if (licenseNumber == null || !licenseNumber.matches("^[A-Z]{2}-\\d{2}-[A-Z]{2}-\\d{4}$")) {
            throw new IllegalArgumentException("Invalid license number format. Must be of format XX-11-XX-1111");
        }
        driver.setLicenseNumber(licenseNumber);

        driver = driverRepository.save(driver);
        logger.debug("Driver created with id {}", driver.getDriverId());
        return driver;
    }


    public Driver getDriverById(Long id) {
        logger.debug("Getting driver with id {}", id);
        return driverRepository.findById(id).orElseThrow();
    }

    public List<Driver> getAllDrivers() {
        logger.debug("Getting all drivers");
        return driverRepository.findAll();
    }

    public Driver updateDriver(Long id, DriverDTO driverDTO) {
        logger.debug("Updating driver with id {}", id);
        Driver driver = getDriverById(id);
        if (driverDTO.getName() != null) {
            driver.setName(driverDTO.getName());
        }
        if (driverDTO.getLicenseNumber() != null && driverDTO.getLicenseNumber().matches("^[A-Z]{2}-\\d{2}-[A-Z]{2}-\\d{4}$")) {
            driver.setLicenseNumber(driverDTO.getLicenseNumber());
        }
        driver.setUpdatedOn(LocalDateTime.now());
        driver = driverRepository.save(driver);
        logger.debug("Driver updated with id {}", driver.getDriverId());
        return driver;
    }

    public void deleteDriver(Long id) {
        logger.debug("Deleting driver with id {}", id);
        driverRepository.deleteById(id);
        logger.debug("Driver deleted with id {}", id);
    }
}

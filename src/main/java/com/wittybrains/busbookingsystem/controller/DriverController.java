package com.wittybrains.busbookingsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.DriverDTO;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.service.DriverService;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final Logger logger = LoggerFactory.getLogger(DriverController.class);

    
    private DriverService driverService;
    
    public DriverController( DriverService driverService) {
         this.driverService = driverService;
    }
    
    /**
     * Endpoint to create a new driver
     *
     * @param driverDTO the driver object to be created
     * @return ResponseEntity object with the created Driver object and HTTP status
     */


    
    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) {
        logger.info("Creating driver with DTO={}", driverDTO);
        Driver driver = driverService.createDriver(driverDTO);
        logger.info("Successfully created driver with id={}", driver.getDriverId());
        DriverDTO createdDriverDTO = new DriverDTO(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDriverDTO);
    }

    /**
     * Endpoint to retrieve a driver by its ID
     *
     * @param id the ID of the driver to be retrieved
     * @return ResponseEntity object with the retrieved Driver object and HTTP status
     */

    
    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        logger.info("Retrieving driver with id={}", id);
        Driver driver = driverService.getDriverById(id);
        logger.info("Found driver with id={}", id);
        DriverDTO driverDTO = new DriverDTO(driver);
        return ResponseEntity.ok(driverDTO);
    }
    /**
     * Endpoint to retrieve all drivers
     *
     * @return ResponseEntity object with a list of all drivers and HTTP status
     */

    
    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        logger.info("Retrieving all drivers");
        List<Driver> drivers = driverService.getAllDrivers();
        logger.info("Found {} drivers", drivers.size());
        List<DriverDTO> driverDTOs = drivers.stream()
                .map(DriverDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(driverDTOs);
    }
    /**
     * Endpoint to update an existing driver
     *
     * @param id the ID of the driver to be updated
     * @param driverDTO the driver object with updated information
     * @return ResponseEntity object with the updated Driver object and HTTP status
     */


    
    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Long id, @RequestBody DriverDTO driverDTO) {
        logger.info("Updating driver with id={} and DTO={}", id, driverDTO);
        Driver driver = driverService.updateDriver(id, driverDTO);
        logger.info("Successfully updated driver with id={}", driver.getDriverId());
        DriverDTO updatedDriverDTO = new DriverDTO(driver);
        return ResponseEntity.ok(updatedDriverDTO);
    }
    /**
     * Endpoint to delete a driver by its ID
     *
     * @param id the ID of the driver to be deleted
     * @return ResponseEntity object with HTTP status
     */

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        logger.info("Deleting driver with id={}", id);
        driverService.deleteDriver(id);
        logger.info("Successfully deleted driver with id={}", id);
        return ResponseEntity.noContent().build();
    }
}



package com.wittybrains.busbookingsystem.controller;

import java.util.List;
import java.util.Optional;
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

import com.wittybrains.busbookingsystem.dto.PassengerDTO;
import com.wittybrains.busbookingsystem.model.Passenger;
import com.wittybrains.busbookingsystem.service.PassengerService;

/**
 * 
 * Controller for managing Passenger related requests
 */
@RestController
@RequestMapping("/passengers")
public class PassengerController {

	private final Logger logger = LoggerFactory.getLogger(PassengerController.class);

	private final PassengerService passengerService;

	public PassengerController(PassengerService passengerService) {
		this.passengerService = passengerService;
	}

	/**
	 * Endpoint to create a new passenger
	 * 
	 * @param passengerDTO the passenger object to be created
	 * @return ResponseEntity object with the created Passenger object and HTTP
	 *         status
	 */
	@PostMapping
	public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO) {
	    Passenger passenger = passengerService.createPassenger(passengerDTO);
	    logger.info("Created passenger with ID: {}", passenger.getPassengerId());
	    return ResponseEntity.status(HttpStatus.CREATED).body(new PassengerDTO(passenger));
	}

	/**
	 * Endpoint to get a passenger by their ID
	 * 
	 * @param passengerId the ID of the passenger to be retrieved
	 * @return ResponseEntity object with the retrieved Passenger object and HTTP
	 *         status
	 */
	@GetMapping("/{passengerId}")
	public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable Long passengerId) {
	    Optional<Passenger> optionalPassenger = passengerService.getPassengerById(passengerId);
	    if (optionalPassenger.isPresent()) {
	        Passenger passenger = optionalPassenger.get();
	        logger.info("Retrieved passenger with ID: {}", passengerId);
	        return ResponseEntity.ok(new PassengerDTO(passenger));
	    } else {
	        logger.warn("Failed to retrieve passenger with ID: {}", passengerId);
	        return ResponseEntity.notFound().build();
	    }
	}

	/**
	 * Endpoint to update a passenger by their ID
	 * 
	 * @param passengerId  the ID of the passenger to be updated
	 * @param passengerDTO the passenger object with updated fields
	 * @return ResponseEntity object with HTTP status
	 */
	@PutMapping("/{passengerId}")
	public ResponseEntity<PassengerDTO> updatePassenger(@PathVariable Long passengerId,
	        @RequestBody PassengerDTO passengerDTO) {
	    Passenger updatedPassenger = passengerService.updatePassenger(passengerId, passengerDTO);
	    logger.info("Updated passenger with ID: {}", passengerId);
	    PassengerDTO updatedPassengerDTO = new PassengerDTO(updatedPassenger);
	    return ResponseEntity.ok(updatedPassengerDTO);
	}


	/**
	 * Endpoint to delete a passenger by their ID
	 * 
	 * @param passengerId the ID of the passenger to be deleted
	 * @return ResponseEntity object with HTTP status
	 */
	@DeleteMapping("/{passengerId}")
	public ResponseEntity<Void> deletePassenger(@PathVariable Long passengerId) {
		Optional<Passenger> optionalPassenger = passengerService.getPassengerById(passengerId);
		if (optionalPassenger.isPresent()) {
			passengerService.deletePassenger(passengerId);
			logger.info("Deleted passenger with ID: {}", passengerId);
			return ResponseEntity.noContent().build();
		} else {
			logger.warn("Failed to delete passenger with ID: {}", passengerId);
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Endpoint to retrieve all passengers
	 * 
	 * @return ResponseEntity object with a list of all Passengers and HTTP status
	 */
	@GetMapping
	public ResponseEntity<List<PassengerDTO>> getPassengers() {
	    List<Passenger> passengers = passengerService.getAllPassengers();
	    List<PassengerDTO> passengerDTOs = passengers.stream()
	    		.map(PassengerDTO::new).collect(Collectors.toList());
	    return ResponseEntity.ok(passengerDTOs);
	}

}
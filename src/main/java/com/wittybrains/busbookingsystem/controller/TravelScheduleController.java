package com.wittybrains.busbookingsystem.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.TravelScheduleDTO;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.model.TravelSchedule;
import com.wittybrains.busbookingsystem.repository.TravelScheduleRepository;
import com.wittybrains.busbookingsystem.service.TravelScheduleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/schedules")
public class TravelScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(TravelScheduleController.class);

	private final TravelScheduleService travelScheduleService;
	private final TravelScheduleRepository scheduleRepository;

	public TravelScheduleController(TravelScheduleService travelScheduleService,
			TravelScheduleRepository scheduleRepository) {
		this.travelScheduleService = travelScheduleService;
		this.scheduleRepository = scheduleRepository;
	}

	/**
	 * 
	 * Endpoint to create a new travel schedule
	 * 
	 * @param travelScheduleDTO the travel schedule object to be created
	 * @return ResponseEntity object with the created TravelScheduleDTO object and
	 *         HTTP status
	 * @throws ParseException if an error occurs while parsing the date
	 */

	@PostMapping
	public ResponseEntity<?> createTravelSchedule(@RequestBody TravelScheduleDTO travelScheduleDTO)
			throws ParseException {
		logger.info("Creating travel schedule with DTO={}", travelScheduleDTO);
		ResponseEntity<?> createdTravelSchedule = travelScheduleService.createSchedule(travelScheduleDTO);

		if (createdTravelSchedule != null) {
			logger.info("Successfully created travel schedule");
			return ResponseEntity.ok(createdTravelSchedule);
		} else {
			logger.error("Failed to create travel schedule");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return an empty body or an
																						// error message here
		}
	}

	/**
	 * 
	 * Endpoint to update an existing travel schedule by ID
	 * 
	 * @param id              the ID of the travel schedule to be updated
	 * @param updatedSchedule the updated TravelScheduleDTO object
	 * @return ResponseEntity object with the updated TravelScheduleDTO object and
	 *         HTTP status
	 * @throws ParseException if an error occurs while parsing the date
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateSchedule(@PathVariable Long id, @RequestBody TravelScheduleDTO updatedSchedule)
			throws ParseException {
		try {
			logger.info("Updating travel schedule with id={}, DTO={}", id, updatedSchedule);
			ResponseEntity<?> savedSchedule = travelScheduleService.updateSchedule(id, updatedSchedule);
			if (savedSchedule != null) {
				logger.info("Successfully updated travel schedule");
				return ResponseEntity.ok(savedSchedule);
			} else {
				logger.warn("Travel schedule not found with id={}", id);
				return ResponseEntity.notFound().build();
			}
		} catch (EntityNotFoundException ex) {
			logger.error("Failed to update travel schedule with id={}", id, ex);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	/**
	 * 
	 * Endpoint to retrieve a travel schedule by ID
	 * 
	 * @param id the ID of the travel schedule to be retrieved
	 * @return ResponseEntity object with the retrieved TravelSchedule object and
	 *         HTTP status
	 */

	@GetMapping("/{id}")
	public ResponseEntity<?> getTravelSchedule(@PathVariable Long id) {
		try {
			logger.info("Fetching travel schedule with id={}", id);
			TravelScheduleDTO travelScheduleDTO = travelScheduleService.getScheduleDTOById(id);
			if (travelScheduleDTO != null) {
				logger.info("Successfully fetched travel schedule with id={}", id);
				return ResponseEntity.ok(travelScheduleDTO);
			} else {
				logger.warn("Travel schedule not found with id={}", id);
				return ResponseEntity.notFound().build();
			}
		} catch (EntityNotFoundException ex) {
			logger.error("Failed to fetch travel schedule with id={}", id, ex);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	/**
	 * 
	 * Endpoint to delete a travel schedule by ID
	 * 
	 * @param id the ID of the travel schedule to be deleted
	 * @return ResponseEntity object with HTTP status
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
		Optional<TravelSchedule> existingScheduleOptional = scheduleRepository.findById(id);
		if (existingScheduleOptional.isPresent()) {
			logger.info("Deleting travel schedule with id={}", id);
			scheduleRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			logger.warn("Travel schedule not found with id={}", id);
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 
	 * Endpoint to search for travel schedules based on given criteria
	 * 
	 * @param searchDTO the TravelScheduleDTO object containing the search criteria
	 * @param page      the page number to be returned (default value is 0)
	 * @param size      the number of items per page to be returned (default value
	 *                  is 3)
	 * @return ResponseEntity object with a page of retrieved TravelSchedule objects
	 *         and HTTP status
	 */

	@GetMapping("/schedules")
	public List<TravelScheduleDTO> searchSchedules(@Valid TravelScheduleDTO searchDTO,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Long busId = searchDTO.getBus() != null ? searchDTO.getBus().getId() : null;
		Long driverId = searchDTO.getDriver() != null ? searchDTO.getDriver().getDriverId() : null;
		Page<TravelSchedule> schedules = scheduleRepository.findByCriteria(searchDTO.getSource(),
				searchDTO.getDestination(), busId, driverId, searchDTO.getEstimatedArrivalTimeStart(),
				searchDTO.getEstimatedArrivalTimeEnd(), searchDTO.getEstimatedDepartureTimeStart(),
				searchDTO.getEstimatedDepartureTimeEnd(), pageable);
		List<TravelScheduleDTO> scheduleDTOs = schedules.stream().map(schedule -> new TravelScheduleDTO(schedule))
				.collect(Collectors.toList());
		return scheduleDTOs;
	}

	/**
	 * 
	 * Endpoint to retrieve all travel schedules
	 * 
	 * @return ResponseEntity object with a list of retrieved TravelScheduleDTO
	 *         objects and HTTP status
	 */
	@GetMapping
	public ResponseEntity<List<TravelScheduleDTO>> getAllSchedules() {
		logger.info("Getting all travel schedules");
		List<TravelScheduleDTO> travelScheduleDTOList = travelScheduleService.getAllSchedules();
		return ResponseEntity.ok().body(travelScheduleDTOList);
	}

	@GetMapping("/travel-schedules/arrival")
	public List<TravelSchedule> getTravelSchedulesByArrivalTime(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String arrivalTime) {
		return scheduleRepository.findByEstimatedArrivalTimeLessThan(arrivalTime);
	}

	@GetMapping("/travel-schedules/departure")
	public List<TravelSchedule> getTravelSchedulesByDepartureTime(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String departureTime) {
		return scheduleRepository.findByEstimatedDepartureTimeLessThan(departureTime);
	}

	@GetMapping("/driver/{driverId}")
	public List<TravelSchedule> getTravelSchedulesByDriver(@PathVariable Long driverId) {
		Driver driver = new Driver(); // create a Driver object with the given ID
		return scheduleRepository.findByDriver(driver);
	}

	@GetMapping("/search")
	public ResponseEntity<List<TravelSchedule>> findBySourceAndDestination(@RequestParam("source") String source,
			@RequestParam("destination") String destination) {
		List<TravelSchedule> travelSchedules = scheduleRepository.findBySourceAndDestination(source, destination);
		if (travelSchedules.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(travelSchedules);
	}
	
	  @GetMapping("/searchBySeatCost")
	    public ResponseEntity<List<TravelSchedule>> findBySeatCostGreaterThan(@RequestParam("cost") double cost) {
	        List<TravelSchedule> travelSchedules = scheduleRepository.findBySeatCostGreaterThan(cost);
	        if (travelSchedules.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok().body(travelSchedules);
	    }
}

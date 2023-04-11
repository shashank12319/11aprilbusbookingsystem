
package com.wittybrains.busbookingsystem.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.dto.TravelScheduleDTO;
import com.wittybrains.busbookingsystem.exception.ResourceNotFoundException;
import com.wittybrains.busbookingsystem.model.Bus;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.model.TravelSchedule;
import com.wittybrains.busbookingsystem.repository.BusRepository;
import com.wittybrains.busbookingsystem.repository.DriverRepository;
import com.wittybrains.busbookingsystem.repository.StationRepository;
import com.wittybrains.busbookingsystem.repository.TravelScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TravelScheduleService {
	private static final Logger logger = LoggerFactory.getLogger(TravelScheduleService.class);
	
	
	List<String> indianCities = Arrays.asList("Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai",
			"Kolkata", "Surat", "Pune", "Jaipur", "Lucknow", "Kanpur", "Nagpur", "Visakhapatnam", "Bhopal", "Patna",
			"Ludhiana", "Agra", "Nashik", "Vadodara", "Faridabad", "Madurai", "Meerut", "Rajkot", "Srinagar",
			"Aurangabad", "Dhanbad", "Amritsar", "Navi Mumbai", "Allahabad", "Ranchi", "Haora", "Coimbatore",
			"Jabalpur", "Gwalior", "Vijayawada", "Jodhpur", "Madras", "Raipur", "Kota", "Guwahati", "Chandigarh",
			"Solapur", "Hubli", "Bareilly", "Moradabad", "Mysore", "Gurgaon", "Aligarh", "Jalandhar",
			"Tiruchirappalli", "Bhubaneswar", "Salem", "Mira Bhayandar", "Thiruvananthapuram", "Bhiwandi",
			"Saharanpur", "Gorakhpur", "Guntur", "Bikaner", "Amravati", "Noida", "Jamshedpur", "Bhilai Nagar",
			"Warangal", "Cuttack", "Firozabad", "Kochi", "Bhavnagar", "Dehradun", "Durgapur", "Asansol", "Nanded",
			"Waghala", "Kolapur", "Ajmer", "Gulbarga", "Jamnagar", "Ujjain", "Loni", "Siliguri", "Jhansi",
			"Ulhasnagar", "Nellore", "Jammu", "Sangli", "Miraj", "Kupwad", "Belgaum", "Mangalore", "Ambattur",
			"Tirunelveli", "Malegaon", "Gaya", "Jalgaon", "Udaipur", "Maheshtala");
	private final TravelScheduleRepository scheduleRepository;
	private final BusRepository busRepository;
	private final DriverRepository driverRepository;

	public TravelScheduleService(TravelScheduleRepository scheduleRepository, StationRepository stationRepository,
			BusRepository busRepository,DriverRepository driverRepository) {
		this.scheduleRepository = scheduleRepository;
		this.busRepository = busRepository;
		this.driverRepository=driverRepository;
	}



	
	 public TravelScheduleDTO getScheduleDTOById(Long id) {
	        Optional<TravelSchedule> optionalSchedule = scheduleRepository.findById(id);
	        if (optionalSchedule.isPresent()) {
	            TravelSchedule travelSchedule = optionalSchedule.get();
	            return new TravelScheduleDTO(travelSchedule);
	        } else {
	            throw new ResourceNotFoundException("Travel schedule not found with id " + id);
	        }
	    }
	public ResponseEntity<?> updateSchedule(Long scheduleId, TravelScheduleDTO travelScheduleDTO)
			throws ParseException {
		logger.info("Updating travel schedule with id {}: {}", scheduleId, travelScheduleDTO);

		// Check if any required field is missing
		if (travelScheduleDTO.getSource() == null || travelScheduleDTO.getDestination() == null
				|| travelScheduleDTO.getBus() == null || travelScheduleDTO.getEstimatedArrivalTime() == null
				|| travelScheduleDTO.getEstimatedDepartureTime() == null) {

			logger.warn("Required field is missing from travel schedule: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required field is missing from travel schedule");
		}
		
		if (StringUtils.isBlank(travelScheduleDTO.getSource()) || StringUtils.isBlank(travelScheduleDTO.getDestination())) {
			throw new IllegalArgumentException("Source and destination cannot be empty");
		}

		// Validate estimated arrival and departure time format
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

		try {
			LocalDateTime estimatedArrivalTime = LocalDateTime.parse(travelScheduleDTO.getEstimatedArrivalTime(),
					formatter);
			LocalDateTime estimatedDepartureTime = LocalDateTime.parse(travelScheduleDTO.getEstimatedDepartureTime(),
					formatter);

			if (estimatedArrivalTime.isBefore(LocalDateTime.now())
					|| estimatedDepartureTime.isBefore(LocalDateTime.now())) {
				logger.warn("Estimated arrival or departure time is in the past: {}", travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival or departure time is in the past");
			}

			if (estimatedArrivalTime.isEqual(estimatedDepartureTime)) {
				logger.warn("Estimated arrival time cannot be equal to estimated departure time: {}",
						travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival time cannot be equal to estimated departure time");
			}

			if (estimatedArrivalTime.isAfter(estimatedDepartureTime)) {
				logger.warn("Estimated arrival time cannot be after estimated departure time: {}", travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival time cannot be after estimated departure time");
			}

		} catch (DateTimeParseException e) {
			logger.warn("Invalid estimated arrival or departure time format: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid estimated arrival or departure time format");
		}

		// Validation: totalseat cannot be more than 500
		if (travelScheduleDTO.getTotalSeat() > 500) {
			logger.warn("Total seats cannot be more than 500: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Total seats cannot be more than 500");
		}

		// Validation: totalseat is always greater than seatbooked
		if (travelScheduleDTO.getSeatBooked() > travelScheduleDTO.getTotalSeat()) {
			logger.warn("Seats booked cannot be more than total seats: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seats booked cannot be more than total seats");
		}

		// Validation: seat cost cannot be more than 1000.0
		if (travelScheduleDTO.getSeatCost() > 1000.0) {
			logger.warn("Seat cost cannot be more than 1000.0: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat cost cannot be more than 1000.0");
		}

		

		if (!indianCities.contains(travelScheduleDTO.getSource())
				|| !indianCities.contains(travelScheduleDTO.getDestination())) {
			logger.warn("Source or destination is not an Indian city: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source or destination is not an Indian city");
		}
		Optional<TravelSchedule> optionalSchedule = scheduleRepository.findById(scheduleId);

		if (!optionalSchedule.isPresent()) {
			logger.warn("Schedule not found with ID: {}", scheduleId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found");
		}

		TravelSchedule existingSchedule = optionalSchedule.get();

		// Update the schedule with the new details

		existingSchedule.setEstimatedArrivalTime(travelScheduleDTO.getEstimatedArrivalTime());
		existingSchedule.setEstimatedDepartureTime(travelScheduleDTO.getEstimatedDepartureTime());
		existingSchedule.setTotalSeat(travelScheduleDTO.getTotalSeat());
		existingSchedule.setSeatBooked(travelScheduleDTO.getSeatBooked());
		existingSchedule.setSeatCost(travelScheduleDTO.getSeatCost());
		existingSchedule.setSource(travelScheduleDTO.getSource());
		existingSchedule.setDestination(travelScheduleDTO.getDestination());
		// Save the updated schedule
		scheduleRepository.save(existingSchedule);

		return ResponseEntity.status(HttpStatus.OK).body(existingSchedule);
	}

	public ResponseEntity<?> createSchedule(TravelScheduleDTO travelScheduleDTO) throws ParseException {
		logger.info("Creating travel schedule: {}", travelScheduleDTO);

		// Check if any required field is missing
		if (travelScheduleDTO.getSource() == null || travelScheduleDTO.getDestination() == null
				|| travelScheduleDTO.getBus() == null || travelScheduleDTO.getEstimatedArrivalTime() == null
				|| travelScheduleDTO.getEstimatedDepartureTime() == null) {

			logger.warn("Required field is missing from travel schedule: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required field is missing from travel schedule");
		}

		// Validate estimated arrival and departure time format
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

		try {
			LocalDateTime estimatedArrivalTime = LocalDateTime.parse(travelScheduleDTO.getEstimatedArrivalTime(),
					formatter);
			LocalDateTime estimatedDepartureTime = LocalDateTime.parse(travelScheduleDTO.getEstimatedDepartureTime(),
					formatter);

			if (estimatedArrivalTime.isBefore(LocalDateTime.now())
					|| estimatedDepartureTime.isBefore(LocalDateTime.now())) {
				logger.warn("Estimated arrival or departure time is in the past: {}", travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival or departure time is in the past");
			}

			if (estimatedArrivalTime.isEqual(estimatedDepartureTime)) {
				logger.warn("Estimated arrival time cannot be equal to estimated departure time: {}",
						travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival time cannot be equal to estimated departure time");
			}

			if (estimatedArrivalTime.isAfter(estimatedDepartureTime)) {
				logger.warn("Estimated arrival time cannot be after estimated departure time: {}", travelScheduleDTO);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Estimated arrival time cannot be after estimated departure time");
			}

		} catch (DateTimeParseException e) {
			logger.warn("Invalid estimated arrival or departure time format: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid estimated arrival or departure time format");
		}

		// Validation: totalseat cannot be more than 500
		if (travelScheduleDTO.getTotalSeat() > 500) {
			logger.warn("Total seats cannot be more than 500: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Total seats cannot be more than 500");
		}

		// Validation: totalseat is always greater than seatbooked
		if (travelScheduleDTO.getSeatBooked() > travelScheduleDTO.getTotalSeat()) {
			logger.warn("Seats booked cannot be more than total seats: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seats booked cannot be more than total seats");
		}

		// Validation: seat cost cannot be more than 1000.0
		if (travelScheduleDTO.getSeatCost() > 1000.0) {
			logger.warn("Seat cost cannot be more than 1000.0: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seat cost cannot be more than 1000.0");
		}

		

		if (!indianCities.contains(travelScheduleDTO.getSource())
				|| !indianCities.contains(travelScheduleDTO.getDestination())) {
			logger.warn("Source or destination is not an Indian city: {}", travelScheduleDTO);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source or destination is not an Indian city");
		}

		TravelSchedule travelSchedule = new TravelSchedule();

		travelSchedule.setSource(travelScheduleDTO.getSource());
		travelSchedule.setDestination(travelScheduleDTO.getDestination());
		travelSchedule.setSeatCost(travelScheduleDTO.getSeatCost());
		travelSchedule.setTotalSeat(travelScheduleDTO.getTotalSeat());
		travelSchedule.setEstimatedArrivalTime(travelScheduleDTO.getEstimatedArrivalTime());
		travelSchedule.setEstimatedDepartureTime(travelScheduleDTO.getEstimatedDepartureTime());

		Bus bus = busRepository.findById(travelScheduleDTO.getBus().getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid bus id"));

		// Set the bus property of travelSchedule
		travelSchedule.setBus(bus);
		
		Driver driver = driverRepository.findById(travelScheduleDTO.getDriver().getDriverId())
		        .orElseThrow(() -> new IllegalArgumentException("Invalid driver id"));

		// Set the driver property of travelSchedule
		travelSchedule.setDriver(driver);
		
		


		// Calculate the number of available seats and the number of seats already
		// booked
		int totalSeats = travelSchedule.getTotalSeat();
		int seatsBooked = travelSchedule.getSeatBooked();
		int availableSeats = totalSeats - seatsBooked;

		// Update the number of seats booked and available seats based on the number of
		// seats being booked
		int requestedSeatCount = travelScheduleDTO.getSeatBooked();
		if (requestedSeatCount > 0 && requestedSeatCount <= availableSeats) {
			seatsBooked += requestedSeatCount;
			availableSeats = availableSeats - requestedSeatCount;
			travelSchedule.setSeatBooked(seatsBooked);
			travelSchedule.setAvailableSeat(availableSeats);
			travelSchedule = scheduleRepository.save(travelSchedule);
			logger.info("Created travel schedule: {}", travelSchedule);
			return ResponseEntity.status(HttpStatus.CREATED).body(travelSchedule);
		} else {
			logger.warn("Cannot book {} seats, only {} seats are available", requestedSeatCount, availableSeats);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"Cannot book " + requestedSeatCount + " seats, only " + availableSeats + " seats are available");
		}

	}

	public List<TravelScheduleDTO> getAllSchedules() {
		List<TravelSchedule> schedules = scheduleRepository.findAll();
		List<TravelScheduleDTO> scheduleDTOs = new ArrayList<>();
		for (TravelSchedule schedule : schedules) {
			scheduleDTOs.add(new TravelScheduleDTO(schedule));
		}
		return scheduleDTOs;
	}

}

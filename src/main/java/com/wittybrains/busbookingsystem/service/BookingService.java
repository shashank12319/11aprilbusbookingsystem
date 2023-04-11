
package com.wittybrains.busbookingsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.stereotype.Service;
import com.wittybrains.busbookingsystem.dto.BookingDTO;
import com.wittybrains.busbookingsystem.dto.UserDTO;
import com.wittybrains.busbookingsystem.exception.TravelScheduleNotFoundException;
import com.wittybrains.busbookingsystem.exception.UserNotFoundException;
import com.wittybrains.busbookingsystem.model.Booking;
import com.wittybrains.busbookingsystem.model.TravelSchedule;
import com.wittybrains.busbookingsystem.model.User;
import com.wittybrains.busbookingsystem.repository.BookingRepository;
import com.wittybrains.busbookingsystem.repository.TravelScheduleRepository;
import com.wittybrains.busbookingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;
	private final TravelScheduleRepository travelScheduleRepository;
	private final EmailNotificationService emailNotificationService;
	private final Logger logger = LoggerFactory.getLogger(BookingService.class);
	

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

	public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
			TravelScheduleRepository travelScheduleRepository, EmailNotificationService emailNotificationService) {
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
		this.travelScheduleRepository = travelScheduleRepository;
		this.emailNotificationService = emailNotificationService;
	}
	
	 
	
	// Update the createBooking method to set the status to "pending" on creation
	public BookingDTO createBooking(BookingDTO bookingDTO) throws MessagingException {
	    logger.info("Creating booking with bookingDTO: {}", bookingDTO);

	    if (bookingDTO.getSchedule() == null || bookingDTO.getSchedule().getId() == null) {
	        throw new IllegalArgumentException("Schedule ID cannot be null");
	    }
	    if (bookingDTO.getUser() == null || bookingDTO.getUser().getId() == null) {
	        throw new IllegalArgumentException("User ID cannot be null");
	    }

	    // Retrieve user and travel schedule
	    UserDTO userDTO = bookingDTO.getUser();
	    Optional<User> optionalUser = userRepository.findById(userDTO.getId());
	    User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found for id: " + userDTO.getId()));
	    Optional<TravelSchedule> optionalTravelSchedule = travelScheduleRepository.findById(bookingDTO.getSchedule().getId());
	    TravelSchedule travelSchedule = optionalTravelSchedule.orElseThrow(() -> new TravelScheduleNotFoundException("Travel schedule not found for id: " + bookingDTO.getSchedule().getId()));

	    // Update travel schedule and save booking
	    travelScheduleRepository.save(travelSchedule);

	    Booking booking = new Booking();
	    booking.setUser(user);
	    booking.setSchedule(travelSchedule);
	    booking.setStatus("pending"); // Set status to "pending" on creation
	    booking.setBookingTime(bookingDTO.getBookingTime());
	    Booking savedBooking = bookingRepository.save(booking);
	    logger.info("Booking created with savedBooking: {}", savedBooking);

	    emailNotificationService.sendBookingNotification("Booking notification message", savedBooking, user.getEmail());

	    return new BookingDTO(savedBooking);
	}




	public BookingDTO getBookingById(Long id) {
		Optional<Booking> bookingOptional = bookingRepository.findById(id);
		Booking booking = bookingOptional.orElseThrow(() -> new RuntimeException("Booking not found for id: " + id));

		return new BookingDTO(booking);
	}

	public void deleteBooking(Long id) {
		Optional<Booking> bookingOptional = bookingRepository.findById(id);
		Booking booking = bookingOptional.orElseThrow(() -> new RuntimeException("Booking not found for id: " + id));

		bookingRepository.delete(booking);
	}

	public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
		Optional<Booking> bookingOptional = bookingRepository.findById(id);
		Booking booking = bookingOptional.orElseThrow();

		if (bookingDTO.getUser() != null && bookingDTO.getUser().getId() != null) {
			Optional<User> optionalUser = userRepository.findById(bookingDTO.getUser().getId());
			User user = optionalUser.orElseThrow(
					() -> new UserNotFoundException("User not found for id: " + bookingDTO.getUser().getId()));
			booking.setUser(user);
		}

		if (bookingDTO.getSchedule() != null && bookingDTO.getSchedule().getId() != null) {
			Optional<TravelSchedule> optionalTravelSchedule = travelScheduleRepository
					.findById(bookingDTO.getSchedule().getId());
			TravelSchedule travelSchedule = optionalTravelSchedule
					.orElseThrow(() -> new TravelScheduleNotFoundException(
							"Travel schedule not found for id: " + bookingDTO.getSchedule().getId()));
			booking.setSchedule(travelSchedule);
		}
		Booking updatedBooking = bookingRepository.save(booking);
		return new BookingDTO(updatedBooking);
	}

	
	

	public List<BookingDTO> getAllBookings() {
		List<Booking> bookings = bookingRepository.findAll();
		List<BookingDTO> bookingDTOs = new ArrayList<>();
		for (Booking booking : bookings) {
			bookingDTOs.add(new BookingDTO(booking));
		}
		return bookingDTOs;
	}

	
}


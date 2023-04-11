 package com.wittybrains.busbookingsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.dto.UserDTO;
import com.wittybrains.busbookingsystem.exception.InvalidInputException;
import com.wittybrains.busbookingsystem.model.User;
import com.wittybrains.busbookingsystem.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String emailUsername;

	public UserService(UserRepository userRepository, JavaMailSender javaMailSender) {
		this.userRepository = userRepository;
		this.javaMailSender = javaMailSender;
	}

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	public UserDTO createUser(UserDTO userDTO) {
		logger.info("Creating user: {}", userDTO);
		String email = userDTO.getEmail();
		String password = userDTO.getPassword();
		String username = userDTO.getUsername();
		String phoneNumber = userDTO.getPhoneNumber();
		String city = userDTO.getCity();
		String address = userDTO.getAddress();
		String country = userDTO.getCountry();

		if (StringUtils.isBlank(email) || StringUtils.isBlank(password) || password.length() < 6
				|| StringUtils.isBlank(username) || StringUtils.isBlank(phoneNumber) || StringUtils.isBlank(city)
				|| StringUtils.isBlank(address) || StringUtils.isBlank(country)) {
			throw new InvalidInputException("Invalid input for creating a user");
		}

		if (!phoneNumber.matches("\\d{10}")) {
			throw new InvalidInputException("Phone number must be a 10-digit number");
		}

		if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			throw new InvalidInputException("Invalid email format");
		}

		if (address.length() < 6) {
			throw new InvalidInputException("Address must be at least 6 characters long");
		}

		if (country.length() < 3) {
			throw new InvalidInputException("Country must be at least 6 characters long");
		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		user.setEmail(email);
		user.setCity(city);
		user.setAddress(address);
		user.setCountry(country);

		User createdUser = userRepository.save(user);
		logger.info("User created: {}", createdUser);

		// Send welcome email to the user with a PDF attachment
		try {
			MimeMessage message = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailUsername);
			helper.setTo(createdUser.getEmail());
			helper.setSubject("Welcome to our bus booking system!");

			String emailText = "Hello " + createdUser.getUsername()
					+ ",\n\nWelcome to our bus reservation system! Please find attached our travel guide in PDF format. We hope this guide will help you make the most of our application.\n\nBest regards,\nThe Bus Reservation System team";
			helper.setText(emailText);

			ClassPathResource pdf = new ClassPathResource("static/OnlineBusTicketReservationSystem.pdf");

			helper.addAttachment("onlinebusticketreservationsystem.pdf", pdf);

			javaMailSender.send(message);
		} catch (Exception ex) {
			logger.error("Failed to send welcome email to user: {}", createdUser.getUsername(), ex);
		}

		return new UserDTO(createdUser);
	}

	public UserDTO getUser(Long userId) {
		logger.info("Retrieving user with ID: {}", userId);
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			logger.info("User retrieved: {}", user);
			return new UserDTO(user);
		} else {
			logger.warn("User not found with ID: {}", userId);
			return null;
		}
	}

	public UserDTO updateUser(Long userId, UserDTO userDTO) {
		logger.info("Updating user with ID: {}", userId);
		if (StringUtils.isBlank(userDTO.getEmail()) || StringUtils.isBlank(userDTO.getPassword())
				|| userDTO.getPassword().length() < 6 || StringUtils.isBlank(userDTO.getUsername())
				|| StringUtils.isBlank(userDTO.getPhoneNumber()) || StringUtils.isBlank(userDTO.getCity())
				|| StringUtils.isBlank(userDTO.getAddress()) || StringUtils.isBlank(userDTO.getCountry())) {
			throw new InvalidInputException("Invalid input for updating a user");
		}
		if (userDTO.getPhoneNumber().length() != 10) {
			throw new InvalidInputException("Phone number must be 10 digits");
		}
		if (!userDTO.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
			throw new InvalidInputException("Invalid email format");
		}
		if (userDTO.getAddress().length() < 6) {
			throw new InvalidInputException("Address must be at least 6 characters long");
		}
		if (userDTO.getCountry().length() < 3) {
			throw new InvalidInputException("Country name must be at least 6 characters long");
		}
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setUsername(userDTO.getUsername());
			user.setPassword(userDTO.getPassword());
			user.setPhoneNumber(userDTO.getPhoneNumber());
			user.setEmail(userDTO.getEmail());
			user.setCity(userDTO.getCity());
			user.setAddress(userDTO.getAddress());
			user.setCountry(userDTO.getCountry());

			User updatedUser = userRepository.save(user);
			logger.info("User updated successfully: {}", updatedUser);
			return new UserDTO(updatedUser);
		} else {
			logger.warn("User with ID: {} not found", userId);
			return null;
		}
	}

	public void deleteUser(Long userId) {
		logger.info("Deleting user with ID: {}", userId);
		userRepository.deleteById(userId);
	}

	public List<UserDTO> getAllUsers() {
		logger.info("Getting all users");
		List<User> userList = userRepository.findAll();
		List<UserDTO> userDTOList = new ArrayList<>();
		for (User user : userList) {
			userDTOList.add(new UserDTO(user));
		}
		return userDTOList;
	}
}
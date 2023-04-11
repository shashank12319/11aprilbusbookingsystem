package com.wittybrains.busbookingsystem.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wittybrains.busbookingsystem.dto.UserDTO;

import com.wittybrains.busbookingsystem.service.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Rest controller for managing User entities.
 */
@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class.getName());

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 
	 * Endpoint to get a specific user by ID.
	 * 
	 * @param userId the ID of the user to retrieve
	 * @return ResponseEntity object with the retrieved UserDTO object and HTTP
	 *         status
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
		logger.log(Level.INFO, "Fetching user with id: " + userId);
		UserDTO userDTO = userService.getUser(userId);
		if (userDTO != null) {
			return ResponseEntity.ok(userDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 
	 * Endpoint to create a new user.
	 * 
	 * @param newUserDTO the UserDTO object to be created
	 * @return ResponseEntity object with the of the created user and HTTP status
	 */
	@PostMapping("/users")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newUserDTO) {
		logger.log(Level.INFO, "Creating user with username: " + newUserDTO.getUsername());
		UserDTO savedUser = userService.createUser(newUserDTO);
		return ResponseEntity.ok(savedUser);
	}

	/**
	 * 
	 * Endpoint to update an existing user by ID.
	 * 
	 * @param userId         the ID of the user to be updated
	 * @param updatedUserDTO the UserDTO object with the updated information
	 * @return ResponseEntity object with the updated UserDTO object and HTTP status
	 */
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO updatedUserDTO) {
		logger.log(Level.INFO, "Updating user with id: " + userId);
		UserDTO updatedUser = userService.updateUser(userId, updatedUserDTO);
		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 
	 * Endpoint to delete a user by ID.
	 * 
	 * @param userId the ID of the user to be deleted
	 * @return ResponseEntity object with HTTP status
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		logger.log(Level.INFO, "Deleting user with id: " + userId);
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 
	 * Endpoint to get a list of all users.
	 * 
	 * @return ResponseEntity object with the list of UserDTO objects and HTTP
	 *         status
	 */
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		logger.log(Level.INFO, "Fetching all users");
		List<UserDTO> userDTOList = userService.getAllUsers();
		if (!userDTOList.isEmpty()) {
			return ResponseEntity.ok(userDTOList);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

package com.wittybrains.busbookingsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wittybrains.busbookingsystem.dto.FeedBackDTO;

import com.wittybrains.busbookingsystem.service.FeedBackService;

import java.util.List;
import javax.validation.Valid;

/**
 * 
 * Controller for submitting and retrieving feedback for a booking
 */
@RestController
@RequestMapping("/api/booking/{bookingId}/feedback")
public class FeedBackController {

	private final FeedBackService feedBackService;

	/**
	 * Constructor for creating FeedBackController object
	 * 
	 * @param feedBackService object of FeedBackService class to handle feedback
	 *                        related operations
	 */
	public FeedBackController(FeedBackService feedBackService) {
		this.feedBackService = feedBackService;
	}

	/**
	 * Endpoint for submitting feedback for a booking
	 * 
	 * @param bookingId   ID of the booking for which feedback is to be submitted
	 * @param feedbackDto Object of FeedBackDTO class containing feedback data
	 * @return ResponseEntity object with FeedBackDTO object and HTTP status
	 */
	@PostMapping
	public ResponseEntity<FeedBackDTO> submitFeedback(@PathVariable Long bookingId,
			@Valid @RequestBody FeedBackDTO feedbackDto) {
		return feedBackService.submitFeedback(bookingId, feedbackDto);
	}

	/**
	 * Endpoint for retrieving feedback for a booking
	 * 
	 * @param bookingId ID of the booking for which feedback is to be retrieved
	 * @return ResponseEntity object with list of FeedBackDTO objects and HTTP
	 *         status
	 */
	@GetMapping
	public ResponseEntity<List<FeedBackDTO>> getFeedbackForBooking(@PathVariable Long bookingId) {
		List<FeedBackDTO> feedbackDTOList = feedBackService.getFeedbackForBooking(bookingId);
		return ResponseEntity.ok(feedbackDTOList);
	}
}

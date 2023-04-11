package com.wittybrains.busbookingsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.dto.FeedBackDTO;
import com.wittybrains.busbookingsystem.model.Booking;
import com.wittybrains.busbookingsystem.model.FeedBack;
import com.wittybrains.busbookingsystem.repository.BookingRepository;
import com.wittybrains.busbookingsystem.repository.FeedBackRepository;

@Service
public class FeedBackService {
    
    private BookingRepository bookingRepository;
    private FeedBackRepository feedBackRepository;
    
    public FeedBackService(BookingRepository bookingRepository, FeedBackRepository feedBackRepository) {
        this.bookingRepository = bookingRepository;
        this.feedBackRepository = feedBackRepository;
    }

    public ResponseEntity<FeedBackDTO> submitFeedback(Long bookingId, FeedBackDTO feedbackDto) {
        // Verify that the booking exists
        Booking booking = bookingRepository.findById(bookingId)
        		.orElseThrow(() -> new RuntimeException("Booking not found "));

        // Parse the feedback DTO for rating and comment
        int rating = feedbackDto.getRating();
        String comment = feedbackDto.getComment();

        // Validate the rating
        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body(new FeedBackDTO("Invalid rating"));
        }

        // Create a new feedback object
        FeedBack feedback = new FeedBack();
        feedback.setBooking(booking);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(LocalDateTime.now());

        // Add the feedback to the database
        feedBackRepository.save(feedback);

        // Return the feedback object in the response
        return ResponseEntity.status(HttpStatus.CREATED).body(new FeedBackDTO(feedback));
    }
    
    public List<FeedBackDTO> getFeedbackForBooking(Long bookingId) {
        // Verify that the booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Retrieve the feedback for the given booking
        List<FeedBack> feedbackList = feedBackRepository.findByBooking(booking);

        // Convert the feedback list to DTOs
        List<FeedBackDTO> feedbackDTOList = feedbackList.stream()
                .map(FeedBackDTO::new)
                .collect(Collectors.toList());

        return feedbackDTOList;
    }
}


package com.wittybrains.busbookingsystem.dto;

import java.time.LocalDateTime;

import com.wittybrains.busbookingsystem.model.FeedBack;

public class FeedBackDTO {
    private Long id;
    private Long bookingId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public FeedBackDTO(String error) {
        this.comment = error;
    }
    
    

    public FeedBackDTO() {
		super();
	}



	public FeedBackDTO(FeedBack feedback) {
        this.id = feedback.getId();
        this.bookingId = feedback.getBooking().getBookingId();
        this.rating = feedback.getRating();
        this.comment = feedback.getComment();
        this.createdAt = feedback.getCreatedAt();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    // Getters and setters
}



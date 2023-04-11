package com.wittybrains.busbookingsystem.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.wittybrains.busbookingsystem.model.Booking;

public class BookingDTO {
	private Long bookingId;

	private TravelScheduleDTO schedule;

	private UserDTO user;
	  private String status;
	  
	  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime bookingTime;
	public BookingDTO() {
		super();
	}

	public UserDTO getUser() {
		return user;
	}

	public BookingDTO(Booking booking) {
		this.bookingId = booking.getBookingId();
		this.schedule = new TravelScheduleDTO(booking.getSchedule());
        this.bookingTime=booking.getBookingTime();
		this.user = new UserDTO(booking.getUser());
		this.setStatus(booking.getStatus());

	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public TravelScheduleDTO getSchedule() {
		return schedule;
	}

	public void setSchedule(TravelScheduleDTO schedule) {
		this.schedule = schedule;
	}

	public void setUser(UserDTO userDTO) {
		this.user = userDTO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

}
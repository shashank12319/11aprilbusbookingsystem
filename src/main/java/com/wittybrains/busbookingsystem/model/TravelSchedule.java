package com.wittybrains.busbookingsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
@Table(name="travelschedule")
public class TravelSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
   

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    private Bus bus;

    @ManyToOne
    private Driver driver;
    @NotBlank
    private String source;
    @NotBlank
	private String destination;
    
    private int availableSeat;
    
    private int seatBooked;
    
    private int totalSeat;
    @NotBlank
    private String estimatedArrivalTime;
    @NotBlank
     private String estimatedDepartureTime;
     @CreationTimestamp
     private LocalDateTime createdOn;
     
     @UpdateTimestamp
     private LocalDateTime updatedOn;
   @NotNull
    private double seatCost;
   
   private double totalAmount;
   
    
    
    public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }




 

	public int getAvailableSeat() {
		return availableSeat;
	}

	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}

	public int getTotalSeat() {
		return totalSeat;
	}

	public void setTotalSeat(int totalSeat) {
		this.totalSeat = totalSeat;
	}

	public double getSeatCost() {
		return seatCost;
	}

	public void setSeatCost(double seatCost) {
		this.seatCost = seatCost;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getSeatBooked() {
		return seatBooked;
	}

	public void setSeatBooked(int seatBooked) {
		this.seatBooked = seatBooked;
	}



	public String getEstimatedArrivalTime() {
		return estimatedArrivalTime;
	}

	public void setEstimatedArrivalTime(String estimatedArrivalTime) {
		this.estimatedArrivalTime = estimatedArrivalTime;
	}

	public String getEstimatedDepartureTime() {
		return estimatedDepartureTime;
	}

	public void setEstimatedDepartureTime(String estimatedDepartureTime) {
		this.estimatedDepartureTime = estimatedDepartureTime;
	}
	@PrePersist
    void prePersist() {
   	 this.createdOn=LocalDateTime.now();
   	
    }
	 
    @PreUpdate
	void preUpdateDateTime() {
		this.updatedOn = LocalDateTime.now();
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}


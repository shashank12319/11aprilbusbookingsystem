package com.wittybrains.busbookingsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L; 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(name = "username", nullable = false, length = 50)
    @Size(min = 6, max = 50)
    private String username;
    
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\+91\\d{10}", message = "Phone number must start with +91 and be 12 digits long")
    private String phoneNumber;

    
    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format")
    private String email;
    
    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    
    @NotNull(message = "City cannot be null")
    @Size(max = 50, message = "City name must be less than or equal to 50 characters")
    private String city;

    @NotNull(message = "Address cannot be null")
    @Size(max = 50, message = "Address must be less than or equal to 50 characters")
    private String address;
    
    @NotNull(message = "Country cannot be null")
    @Pattern(regexp = "India", message = "Country must be India")
    private String country;
    
    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;
    
    @OneToOne(mappedBy="user")
    private Passenger Passenger;
    
   
    
    @OneToOne(mappedBy="user")
    private Driver driver;
    
    
    
    public Passenger getPassenger() {
		return Passenger;
	}

	public void setPassenger(Passenger passenger) {
		Passenger = passenger;
	}

	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getEmail() {
        return email; 
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PrePersist
    void prePersist() {
        this.createdOn = LocalDateTime.now();
        
    }
   
   @PreUpdate
	void preUpdateDateTime() {
		this.updatedOn = LocalDateTime.now();
	}
}

package com.wittybrains.busbookingsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
@Table(name="passenger")
public class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;
    
    @NotNull(message = "Name cannot be null")
    @Size(min = 6, message = "Name must be at least 6 characters long")
    private String name;
    
    @Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", message = "Contact number must be in Indian format")
    //+91-9876543210", "091-876543210", "7890123456", and "919876543210"
    private String contactNumber;
   
    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[\\w-_.+]*[\\w-_.]@([\\w]+[.])+[\\w]+[\\w]$", message = "Email should be of proper format")
    private String email;
    
   
    
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;
    
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "bus")
    private List<Seat> seats;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    @PrePersist
    void prePersist() {
   	 this.createdOn=LocalDateTime.now();
  
    }
	
	
   @PreUpdate
	void preUpdateDateTime() {
		this.updatedOn = LocalDateTime.now();
	}

}

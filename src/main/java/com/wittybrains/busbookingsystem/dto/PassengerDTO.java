package com.wittybrains.busbookingsystem.dto;

import com.wittybrains.busbookingsystem.model.Passenger;

public class PassengerDTO {

   
    private String name;

   
    private String contactNumber;

    
    private String email;

    
    private String password;

    public PassengerDTO() {
    }

    
    public PassengerDTO(Passenger passenger) {
        this.name = passenger.getName();
        this.contactNumber = passenger.getContactNumber();
        this.email = passenger.getEmail();
        this.password = passenger.getPassword();
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

    // getters and setters
}

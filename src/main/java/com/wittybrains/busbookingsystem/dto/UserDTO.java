package com.wittybrains.busbookingsystem.dto;

import com.wittybrains.busbookingsystem.model.User;

public class UserDTO {
	private Long id;
	private String username;
	private String password;
	private String phoneNumber;
	private String email;

	private String city;
	private String address;
	private String country;

	public UserDTO() {
	}

	public UserDTO(User user) {
		if (user != null) {
			this.id = user.getUserId();
			this.username = user.getUsername();
			this.password = user.getPassword();
			this.phoneNumber = user.getPhoneNumber();
			this.email = user.getEmail();

			this.city = user.getCity();
			this.address = user.getAddress();
			this.country = user.getCountry();

		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
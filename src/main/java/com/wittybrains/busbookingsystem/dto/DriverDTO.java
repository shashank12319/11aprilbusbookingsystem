package com.wittybrains.busbookingsystem.dto;

import com.wittybrains.busbookingsystem.model.Driver;

public class DriverDTO {
    private String name;
   private String licenseNumber;

    private Long driverId;
    


    
    

	public DriverDTO(Driver driver) {
		if (driver != null) {
		this.name = driver.getName();
		this.driverId = driver.getDriverId() != null ? driver.getDriverId() : 0L;
		this.licenseNumber=driver.getLicenseNumber();
	}
	}
	

	public DriverDTO() {
		super();
	}



	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	
}


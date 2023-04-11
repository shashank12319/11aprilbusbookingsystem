package com.wittybrains.busbookingsystem.dto;

import com.wittybrains.busbookingsystem.model.Station;


public class StationDTO {
    private Long id;
    private String name;
    private String stationCode;
 
    private String address;
    private Double latitude;
    private Double longitude;
    
    public StationDTO() {}
    
    public StationDTO(Station station) {
        if(station != null) {
            this.id = station.getId();
            this.name = station.getName();
            this.stationCode = station.getStationCode();
           
            this.address = station.getAddress();
            this.latitude = station.getLatitude();
            this.longitude = station.getLongitude();
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
    
    // getters and setters
}


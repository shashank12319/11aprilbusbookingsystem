package com.wittybrains.busbookingsystem.model;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "stations")
public class Station  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "name")
    @Size(min = 6, max = 50)
    private String name;
    @NotNull
    @Column(name = "station_code",unique = true)
    @Size(min = 6, max = 50)
    private String stationCode;
    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;
    @NotNull
    @Column(name = "address")
    @Size(min = 6, max = 50)
    private String address;
    @NotNull
    @Column(name = "latitude")
    private Double latitude;
    @NotNull
    @Column(name = "longitude")
    private Double longitude;

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

	public Station(String name, String stationCode) {
        this.name = name;
        this.stationCode = stationCode;
    }

    public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Station() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

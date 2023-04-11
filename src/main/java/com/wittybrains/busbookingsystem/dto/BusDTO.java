package com.wittybrains.busbookingsystem.dto;


import com.wittybrains.busbookingsystem.model.Bus;
import com.wittybrains.busbookingsystem.model.BusType;




public class BusDTO {
  
    private String number;
   
    private BusType type;
    private Long id;
    private int capacity;

    public BusDTO() {
    }

    public BusDTO(Bus bus) {
        
        this.number = bus.getNumber();
        this.type = bus.getType();
        this.capacity = bus.getCapacity();
        this.id=bus.getId();
    }


	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public BusType getType() {
		return type;
	}

	public void setType(BusType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

   
}

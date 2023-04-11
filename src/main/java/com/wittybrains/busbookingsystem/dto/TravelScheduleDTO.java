package com.wittybrains.busbookingsystem.dto;




import com.wittybrains.busbookingsystem.model.TravelSchedule;




public class TravelScheduleDTO {


	
	private String estimatedArrivalTime;
	private String estimatedDepartureTime;
	private String estimatedArrivalTimeStart;
    private String estimatedArrivalTimeEnd;
    private String estimatedDepartureTimeStart;
    private String estimatedDepartureTimeEnd;
	
	private String source;
	private String destination;
	private Long id;
   
	private DriverDTO driver;
	private BusDTO bus;
    private int seatBooked;
    private int totalSeat;
    private double SeatCost;
    private int  availableSeat;
    private double totalAmount;
	public TravelScheduleDTO() {
		super();
	}

	public TravelScheduleDTO(TravelSchedule travelSchedule) {
		super();
		if (travelSchedule != null) {


			this.id=travelSchedule.getScheduleId();
			this.source=travelSchedule.getSource();
			this.destination=travelSchedule.getDestination();
            this.totalSeat=travelSchedule.getTotalSeat();
			this.estimatedArrivalTime = travelSchedule.getEstimatedArrivalTime().toString();
			this.estimatedDepartureTime = travelSchedule.getEstimatedDepartureTime().toString();
            
			this.availableSeat=travelSchedule.getAvailableSeat();
			this.setSeatBooked(travelSchedule.getSeatBooked());
			
			this.setBus(new BusDTO(travelSchedule.getBus()));
			this.setDriver(new DriverDTO(travelSchedule.getDriver()));
			this.SeatCost=travelSchedule.getSeatCost();
			 this.totalAmount = this.seatBooked * this.SeatCost * 1.12;
		}

	}

	public TravelScheduleDTO(String string) {
		// TODO Auto-generated constructor stub
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public double getSeatCost() {
		return SeatCost;
	}

	public void setSeatCost(double seatCost) {
		SeatCost = seatCost;
	}

	  public BusDTO getBus() {
	        return bus;
	    }

	    public void setBus(BusDTO bus) {
	        this.bus = bus;
	    }

		public DriverDTO getDriver() {
			return driver;
		}

		public void setDriver(DriverDTO driver) {
			this.driver = driver;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public String getEstimatedArrivalTimeStart() {
			return estimatedArrivalTimeStart;
		}

		public void setEstimatedArrivalTimeStart(String estimatedArrivalTimeStart) {
			this.estimatedArrivalTimeStart = estimatedArrivalTimeStart;
		}

		public String getEstimatedArrivalTimeEnd() {
			return estimatedArrivalTimeEnd;
		}

		public void setEstimatedArrivalTimeEnd(String estimatedArrivalTimeEnd) {
			this.estimatedArrivalTimeEnd = estimatedArrivalTimeEnd;
		}

		public String getEstimatedDepartureTimeStart() {
			return estimatedDepartureTimeStart;
		}

		public void setEstimatedDepartureTimeStart(String estimatedDepartureTimeStart) {
			this.estimatedDepartureTimeStart = estimatedDepartureTimeStart;
		}

		public String getEstimatedDepartureTimeEnd() {
			return estimatedDepartureTimeEnd;
		}

		public void setEstimatedDepartureTimeEnd(String estimatedDepartureTimeEnd) {
			this.estimatedDepartureTimeEnd = estimatedDepartureTimeEnd;
		}




	

	

	

}
package com.wittybrains.busbookingsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.dto.StationDTO;
import com.wittybrains.busbookingsystem.exception.ResourceNotFoundException;
import com.wittybrains.busbookingsystem.exception.StationNotFoundException;
import com.wittybrains.busbookingsystem.model.Station;
import com.wittybrains.busbookingsystem.repository.StationRepository;



@Service
public class StationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StationService.class);

	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public String createStation(StationDTO stationDTO) {
		if (StringUtils.isBlank(stationDTO.getStationCode())) {
			throw new IllegalArgumentException("Station code must not be null or empty");
		}
		Optional<Station> optionalStation = stationRepository.findByStationCode(stationDTO.getStationCode());
		if (optionalStation.isPresent()) {
			throw new IllegalArgumentException("Station code must be unique");
		}
		Station station = new Station(stationDTO.getName(), stationDTO.getStationCode());
		stationRepository.save(station);
		LOGGER.info("Created station with ID: {}", station.getId());
		return "Successfully created station with ID: " + station.getId();
	}

	public Station getStationByCode(String code) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("Station code must not be null or empty");
		}
		Optional<Station> optionalStation = stationRepository.findByStationCode(code);
		if (optionalStation.isPresent()) {
			Station station = optionalStation.get();
			LOGGER.debug("Found station with code {}: {}", code, station.getName());
			return station;
		} else {
			LOGGER.warn("Station with code {} not found", code);
			throw new StationNotFoundException("Station with code " + code + " not found");
		}
	}
	

	
	public List<StationDTO> createStations(List<StationDTO> stationList) {
        List<StationDTO> createdStations = new ArrayList<>();
        
        for (StationDTO stationDTO : stationList) {
        	 LOGGER.debug("Creating station with name {}", stationDTO.getName());
            Station station = new Station();

            String name = stationDTO.getName();
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("Station name cannot be blank.");
            }
            station.setName(name);

            String stationCode = stationDTO.getStationCode();
            if (StringUtils.isBlank(stationCode)) {
                throw new IllegalArgumentException("Station code cannot be blank.");
            }
            station.setStationCode(stationCode);

            String address = stationDTO.getAddress();
            if (StringUtils.isBlank(address)) {
                throw new IllegalArgumentException("Station address cannot be blank.");
            } else if (address.length() < 6) {
                throw new IllegalArgumentException("Station address must be at least 6 characters long.");
            }
            station.setAddress(address);

           

            Double latitude = stationDTO.getLatitude();
            if (latitude == null) {
                throw new IllegalArgumentException("Station latitude cannot be null.");
            } else if (Double.isNaN(latitude) || Double.isInfinite(latitude)) {
                throw new IllegalArgumentException("Station latitude must be a number.");
            }
            station.setLatitude(latitude);

            Double longitude = stationDTO.getLongitude();
            if (longitude == null) {
                throw new IllegalArgumentException("Station longitude cannot be null.");
            } else if (Double.isNaN(longitude) || Double.isInfinite(longitude)) {
                throw new IllegalArgumentException("Station longitude must be a number.");
            }
            station.setLongitude(longitude);
            
            stationRepository.save(station);
            LOGGER.debug("Created station with ID {}", station.getId());
            
            createdStations.add(new StationDTO(station));
            
        }
        LOGGER.debug("Created {} stations", createdStations.size());
        return createdStations;
    }
  
  public StationDTO getStationById(Long id) {
	  LOGGER.info("Getting station by id: {}", id);

        Optional<Station> optionalStation = stationRepository.findById(id);
        if (!optionalStation.isPresent()) {
            throw new ResourceNotFoundException("Station not found with id: " + id);
        }
        
        Station station = optionalStation.get();
        LOGGER.info("Got station: {}", station);
        return new StationDTO(station);
    }
	    

	    public void deleteStation(Long id) {
	    	 LOGGER.info("Deleting station by id: {}", id);
	        Optional<Station> optionalStation = stationRepository.findById(id);
	        if (!optionalStation.isPresent()) {
	            throw new ResourceNotFoundException("Station not found with id: " + id);
	        }
	        
	        Station station = optionalStation.get();
	        stationRepository.delete(station);
	        LOGGER.info("Deleted station: {}", station);
	    }
  
  
	    public StationDTO updateStation(Long id, StationDTO stationDTO) {
	        LOGGER.info("Updating station with id: {} and stationDTO: {}", id, stationDTO);
	        Optional<Station> optionalStation = stationRepository.findById(id);
	        if (!optionalStation.isPresent()) {
	            throw new ResourceNotFoundException("Station not found with id: " + id);
	        }

	        Station station = optionalStation.get();

	        String name = stationDTO.getName();
	        if (StringUtils.isBlank(name)) {
	            throw new IllegalArgumentException("Station name cannot be blank.");
	        }
	        station.setName(name);

	        String stationCode = stationDTO.getStationCode();
	        if (StringUtils.isBlank(stationCode)) {
	            throw new IllegalArgumentException("Station code cannot be blank.");
	        }
	        station.setStationCode(stationCode);

	        String address = stationDTO.getAddress();
	        if (StringUtils.isBlank(address)) {
	            throw new IllegalArgumentException("Station address cannot be blank.");
	        } else if (address.length() < 6) {
	            throw new IllegalArgumentException("Station address must be at least 6 characters long.");
	        }
	        station.setAddress(address);

	        Double latitude = stationDTO.getLatitude();
	        if (latitude == null) {
	            throw new IllegalArgumentException("Station latitude cannot be null.");
	        } else if (Double.isNaN(latitude) || Double.isInfinite(latitude)) {
	            throw new IllegalArgumentException("Station latitude must be a number.");
	        }
	        station.setLatitude(latitude);

	        Double longitude = stationDTO.getLongitude();
	        if (longitude == null) {
	            throw new IllegalArgumentException("Station longitude cannot be null.");
	        } else if (Double.isNaN(longitude) || Double.isInfinite(longitude)) {
	            throw new IllegalArgumentException("Station longitude must be a number.");
	        }
	        station.setLongitude(longitude);

	        stationRepository.save(station);
	        LOGGER.info("Updated station: {}", station);
	        return new StationDTO(station);
	    }
	    public List<StationDTO> getAllStations() {
	        LOGGER.info("Getting all stations");
	        List<Station> stationList = stationRepository.findAll();
	        List<StationDTO> stationDTOList = new ArrayList<>();
	        for (Station station : stationList) {
	            stationDTOList.add(new StationDTO(station));
	        }
	        return stationDTOList;
	    }
	}
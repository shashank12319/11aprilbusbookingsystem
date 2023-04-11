package com.wittybrains.busbookingsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.wittybrains.busbookingsystem.dto.BusDTO;
import com.wittybrains.busbookingsystem.model.Bus;
import com.wittybrains.busbookingsystem.repository.BusRepository;

@Service
public class BusService {

	private final BusRepository busRepository;
	private final Logger logger = LoggerFactory.getLogger(BusService.class);

	public BusService(BusRepository busRepository) {
		this.busRepository = busRepository;
	}

	public List<BusDTO> createBuses(List<BusDTO> busList) {
		List<BusDTO> createdBuses = new ArrayList<>();

		for (BusDTO busDTO : busList) {
			if (StringUtils.isBlank(busDTO.getNumber())) {
				throw new IllegalArgumentException("Bus number cannot be null or empty");
			}

			if (StringUtils.isBlank(busDTO.getNumber())) {
				throw new IllegalArgumentException("Bus number cannot be null or empty");
			}
			if (busDTO.getCapacity() <= 0) {
				throw new IllegalArgumentException("Bus capacity must be a positive integer");
			}
			if (busDTO.getType() == null) {
				throw new IllegalArgumentException("Bus type cannot be null");
			}

			Bus bus = new Bus();
			bus.setNumber(busDTO.getNumber());
			bus.setCapacity(busDTO.getCapacity());
			bus.setType(busDTO.getType());
			busRepository.save(bus);

			createdBuses.add(new BusDTO(bus));
			logger.info("New bus created: {}", busDTO);
		}

		return createdBuses;
	}

	public BusDTO updateBus(Long id, BusDTO busDTO) {
		Bus bus = busRepository.findById(id).orElseThrow();

		// Check that each property is not null using StringUtils
		if (StringUtils.isNotBlank(busDTO.getNumber())) {
			bus.setNumber(busDTO.getNumber());
		}

		if (StringUtils.isBlank(busDTO.getNumber())) {
			throw new IllegalArgumentException("Bus number cannot be null or empty");
		}
		if (busDTO.getCapacity() <= 0) {
			throw new IllegalArgumentException("Bus capacity must be a positive integer");
		}
		if (busDTO.getType() == null) {
			throw new IllegalArgumentException("Bus type cannot be null");
		}

		bus.setCapacity(busDTO.getCapacity());

		bus.setType(busDTO.getType());

		busRepository.save(bus);

		return new BusDTO(bus);
	}

	public BusDTO getBusById(Long id) {
		Bus bus = busRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Bus not found"));
		return new BusDTO(bus);
	}

	public void deleteBus(Long id) {
		Bus bus = busRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Bus not found"));

		busRepository.delete(bus);
		logger.info("Bus with id {} deleted", id);
	}

	public List<BusDTO> getAllBuses() {
		logger.info("Getting all buses");
		List<Bus> busList = busRepository.findAll();
		List<BusDTO> busDTOList = new ArrayList<>();
		for (Bus bus : busList) {
			busDTOList.add(new BusDTO(bus));
		}
		return busDTOList;
	}

}

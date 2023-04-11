package com.wittybrains.busbookingsystem.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wittybrains.busbookingsystem.model.Bus;
import com.wittybrains.busbookingsystem.model.Driver;
import com.wittybrains.busbookingsystem.model.TravelSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

	@Query("SELECT ts FROM TravelSchedule ts WHERE ts.estimatedArrivalTime < :arrivalTime")
	List<TravelSchedule> findByEstimatedArrivalTimeLessThan(@Param("arrivalTime") String arrivalTime);

	@Query("SELECT ts FROM TravelSchedule ts WHERE ts.estimatedDepartureTime < :departureTime")
	List<TravelSchedule> findByEstimatedDepartureTimeLessThan(@Param("departureTime") String departureTime);

	@Query("SELECT ts FROM TravelSchedule ts WHERE ts.driver = :driver")
	List<TravelSchedule> findByDriver(Driver driver);

	@Query("SELECT ts FROM TravelSchedule ts WHERE ts.source = :source AND ts.destination = :destination")
	List<TravelSchedule> findBySourceAndDestination(String source, String destination);

	@Query("SELECT ts FROM TravelSchedule ts WHERE ts.seatCost > :cost")
	List<TravelSchedule> findBySeatCostGreaterThan(double cost);

	// define the query code as a default method
	default Page<TravelSchedule> findByCriteria(String source, String destination, Long busId, Long driverId,
			String estimatedArrivalTimeStart, String estimatedArrivalTimeEnd, String estimatedDepartureTimeStart,
			String estimatedDepartureTimeEnd, Pageable pageable) {

		Specification<TravelSchedule> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (source != null) {
				predicates.add(cb.equal(root.get("source"), source));
			}

			if (destination != null) {
				predicates.add(cb.equal(root.get("destination"), destination));
			}

			if (busId != null) {
				Join<TravelSchedule, Bus> busJoin = root.join("bus");
				predicates.add(cb.equal(busJoin.get("id"), busId));
			}

			if (driverId != null) {
				Join<TravelSchedule, Driver> driverJoin = root.join("driver");
				predicates.add(cb.equal(driverJoin.get("driverId"), driverId));
			}

			if (estimatedArrivalTimeStart != null && estimatedArrivalTimeEnd != null) {
				predicates.add(cb.between(root.get("estimatedArrivalTime"), estimatedArrivalTimeStart,
						estimatedArrivalTimeEnd));
			} else if (estimatedArrivalTimeStart != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("estimatedArrivalTime"), estimatedArrivalTimeStart));
			} else if (estimatedArrivalTimeEnd != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("estimatedArrivalTime"), estimatedArrivalTimeEnd));
			}

			if (estimatedDepartureTimeStart != null && estimatedDepartureTimeEnd != null) {
				predicates.add(cb.between(root.get("estimatedDepartureTime"), estimatedDepartureTimeStart,
						estimatedDepartureTimeEnd));
			} else if (estimatedDepartureTimeStart != null) {
				predicates
						.add(cb.greaterThanOrEqualTo(root.get("estimatedDepartureTime"), estimatedDepartureTimeStart));
			} else if (estimatedDepartureTimeEnd != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("estimatedDepartureTime"), estimatedDepartureTimeEnd));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return findAll(spec, pageable);
	}

	Page<TravelSchedule> findAll(Specification<TravelSchedule> spec, Pageable pageable);

}

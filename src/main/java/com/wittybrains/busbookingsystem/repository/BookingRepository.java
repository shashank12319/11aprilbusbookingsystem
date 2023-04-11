package com.wittybrains.busbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wittybrains.busbookingsystem.model.Booking;
import com.wittybrains.busbookingsystem.model.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findAllByStatusIn(List<String> statuses);

	List<Booking> findByUser(User user);

	



}

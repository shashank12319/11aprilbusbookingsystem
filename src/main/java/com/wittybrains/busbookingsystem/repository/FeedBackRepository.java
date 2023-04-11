package com.wittybrains.busbookingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wittybrains.busbookingsystem.model.Booking;
import com.wittybrains.busbookingsystem.model.FeedBack;


public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
	
	List<FeedBack> findByBooking(Booking booking);
}


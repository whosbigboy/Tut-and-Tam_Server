package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, String> {
    List<Tour> findByActiveTrue();
}

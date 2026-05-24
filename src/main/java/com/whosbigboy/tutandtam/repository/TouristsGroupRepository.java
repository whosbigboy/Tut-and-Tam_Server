package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.TouristsGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TouristsGroupRepository extends JpaRepository<TouristsGroup, String> {
    Optional<TouristsGroup> findFirstByTouristId(String touristId);

    List<TouristsGroup> findByGroupId(String groupId);
}

package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, String> {
}

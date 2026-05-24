package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
}

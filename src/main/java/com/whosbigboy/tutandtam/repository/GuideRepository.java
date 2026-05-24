package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, String> {
    Optional<Guide> findByEmail(String email);

    Optional<Guide> findBySocialId(String socialId);
}

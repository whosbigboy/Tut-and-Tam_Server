package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TouristRepository extends JpaRepository<Tourist, String> {
    Optional<Tourist> findBySocialId(String socialId);

    List<Tourist> findByIdIn(Collection<String> ids);
}

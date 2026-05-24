package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, String> {
    Optional<GroupEntity> findByGuideId(String guideId);
}

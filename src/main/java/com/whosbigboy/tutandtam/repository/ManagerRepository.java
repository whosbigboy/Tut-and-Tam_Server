package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    Optional<Manager> findByEmail(String email);
}

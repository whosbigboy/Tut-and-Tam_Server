package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, String> {
    Optional<Contract> findByNumHash(String numHash);
}

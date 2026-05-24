package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.MemberList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberListRepository extends JpaRepository<MemberList, String> {
    List<MemberList> findByContractId(String contractId);
}

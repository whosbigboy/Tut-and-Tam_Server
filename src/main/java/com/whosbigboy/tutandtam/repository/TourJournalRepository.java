package com.whosbigboy.tutandtam.repository;

import com.whosbigboy.tutandtam.entity.TourJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourJournalRepository extends JpaRepository<TourJournal, String> {
    List<TourJournal> findByTourIdOrderByStartDateAsc(String tourId);

    Optional<TourJournal> findFirstByTourIdOrderByFinishDateDesc(String tourId);
}

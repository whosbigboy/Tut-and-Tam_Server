package com.whosbigboy.tutandtam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "tour_journal")
public class TourJournal {

    @Id
    @Column(name = "uuid", length = 36)
    private String id;

    @Column(name = "tours_uuid", nullable = false, length = 36)
    private String tourId;

    @Column(name = "events_uuid", nullable = false, length = 36)
    private String eventId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "finish_date")
    private Instant finishDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Instant finishDate) {
        this.finishDate = finishDate;
    }
}

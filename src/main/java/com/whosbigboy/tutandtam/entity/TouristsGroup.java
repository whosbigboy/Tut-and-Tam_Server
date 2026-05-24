package com.whosbigboy.tutandtam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tourists_groups")
public class TouristsGroup {

    @Id
    @Column(name = "uuid", length = 36)
    private String id;

    @Column(name = "tourists_uuid", nullable = false, length = 36)
    private String touristId;

    @Column(name = "groups_uuid", nullable = false, length = 36)
    private String groupId;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}

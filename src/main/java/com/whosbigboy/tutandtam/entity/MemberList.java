package com.whosbigboy.tutandtam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_list")
public class MemberList {

    @Id
    @Column(name = "uuid", length = 36)
    private String id;

    @Column(name = "contracts_uuid", nullable = false, length = 36)
    private String contractId;

    @Column(name = "tourists_uuid", nullable = false, length = 36)
    private String touristId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }
}

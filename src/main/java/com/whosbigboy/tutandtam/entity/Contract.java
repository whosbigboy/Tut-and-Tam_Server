package com.whosbigboy.tutandtam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @Column(name = "uuid", length = 36)
    private String id;

    @Column(name = "num_hash", nullable = false, unique = true, length = 100)
    private String numHash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumHash() {
        return numHash;
    }

    public void setNumHash(String numHash) {
        this.numHash = numHash;
    }
}

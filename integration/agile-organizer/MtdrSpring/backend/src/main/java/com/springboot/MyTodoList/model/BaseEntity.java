package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "deleted", nullable = true)
    private Boolean deleted = false;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();


    //empty json getter
    @JsonGetter("deleted")
    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    //getters
    public int getID() { return id; }
    public OffsetDateTime getCreation_ts() { return createdAt; }

    //setters
    public void setID(int id) { this.id = id; }
    public void setCreation_ts(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

}
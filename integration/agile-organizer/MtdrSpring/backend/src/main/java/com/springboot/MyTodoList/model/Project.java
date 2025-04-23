package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PROJECTS")
public class Project extends BaseEntity {
    private String name;
    private String description;
    private OffsetDateTime due_date;

    @ManyToOne(optional = true)
    @JoinColumn(name = "state_id", nullable = true)
    private State state;

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public OffsetDateTime getStartDate() { return getCreation_ts(); }
    public OffsetDateTime getEndDate() { return due_date; }
    public State getState() { return state; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setEndDate(OffsetDateTime dueDate) { this.due_date = dueDate; }
    public void setState(State state) { this.state = state; }
}

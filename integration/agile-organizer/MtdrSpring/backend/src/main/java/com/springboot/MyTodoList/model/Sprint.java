package com.springboot.MyTodoList.model;


import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "SPRINTS")
public class Sprint extends BaseEntity {
    private String name;
    private OffsetDateTime dueDate;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "state_id", nullable = true)
    private State state;

    @ManyToOne(optional = true)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    // Getters
    public String getName() { return name; }
    public OffsetDateTime getDueDate() { return dueDate; }
    public State getState() { return state; }
    public Project getProject() { return project; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDueDate(OffsetDateTime dueDate) { this.dueDate = dueDate; }
    public void setState(State state) { this.state = state; }
    public void setProject(Project project) { this.project = project; }
}

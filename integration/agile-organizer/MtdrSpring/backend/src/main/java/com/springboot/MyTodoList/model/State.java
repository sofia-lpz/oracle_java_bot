package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "STATES")
public class State extends BaseEntity{

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Column(name = "workflow_priority", nullable = true)
    private int workflow_priority;

    public State() {}

    public State(String name, String description, int workflow_priority) {
        this.name = name;
        this.description = description;
        this.workflow_priority = workflow_priority;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWorkflow_priority() {
        return workflow_priority;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorkflow_priority(int workflow_priority) {
        this.workflow_priority = workflow_priority;
    }

}

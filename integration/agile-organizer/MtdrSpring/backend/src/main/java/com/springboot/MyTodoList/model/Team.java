package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "TEAMS")
public class Team extends BaseEntity {
    private String name;
    private String description;

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}

package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "TEAMS")
public class Team extends BaseEntity {
    private String name;
    private String description;

    // Getters
    public String getTeamName() { return name; }
    public String getTeamDescription() { return description; }

    // Setters
    public void setTeamName(String name) { this.name = name; }
    public void setTeamDescription(String description) { this.description = description; }
}

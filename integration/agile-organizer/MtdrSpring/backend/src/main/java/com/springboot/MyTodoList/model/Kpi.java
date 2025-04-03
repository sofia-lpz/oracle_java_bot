package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "kpi")
public class Kpi extends BaseEntity{
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "total")
    private Double total;
    
    @Column(name = "sum")
    private Double sum;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = true)
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = true)
    private Project project;
    
    @ManyToOne
    @JoinColumn(name = "sprint_id", referencedColumnName = "id", nullable = true)
    private Sprint sprint;

    public Kpi() {
    }
    public Kpi(String type, Double total, Double sum, User user, Team team, Project project, Sprint sprint) {
        this.type = type;
        this.total = total;
        this.sum = sum;
        this.user = user;
        this.team = team;
        this.project = project;
        this.sprint = sprint;
    }

    //getters

    public String getType() {
        return type;
    }

    public Double getTotal() {
        return total;
    }

    public Double getSum() {
        return sum;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public Project getProject() {
        return project;
    }

    public Sprint getSprint() {
        return sprint;
    }

    //setters
    public void setType(String type) {
        this.type = type;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
    
}
package com.springboot.MyTodoList.model;

import javax.persistence.*;

import java.time.Instant;
import java.time.OffsetDateTime;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.BaseEntity;
import com.springboot.MyTodoList.model.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TODO")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ToDoItem {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    private Instant createdAt = Instant.now();

    private String title;
    private String description;

    private State state;
    private Sprint sprint;

    private User user;
    private Project project;
    private int storyPoints;

    private OffsetDateTime dueDate;

    private float estimatedHours;
    private float realHours;

    private Boolean done;

    //getters and seters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setCreation_ts(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public float getRealHours() {
        return realHours;
    }

    public void setRealHours(float realHours) {
        this.realHours = realHours;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean isDone() {
        return done;
    }

    
    public int getID() {
        return id;
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

}

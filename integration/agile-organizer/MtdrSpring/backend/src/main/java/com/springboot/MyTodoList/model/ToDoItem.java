package com.springboot.MyTodoList.model;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.User;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.springboot.MyTodoList.model.BaseEntity;
import com.springboot.MyTodoList.model.Project;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "todo")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Column(name = "creation_date", nullable = true)
    private OffsetDateTime creationDate;

    @Column(name = "due_date", nullable = true)
    private OffsetDateTime dueDate;

    @ManyToOne(optional = true)
    @JoinColumn(name = "state_id", nullable = true)
    private State state;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sprint_id", nullable = true)
    private Sprint sprint;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @Column(name = "story_points", nullable = true)
    private Integer storyPoints;  // Changed from int to Integer to allow nulls

    @Column(name = "priority", nullable = true)
    private String priority;

    @Column(name = "deleted", nullable = true)
    private Boolean deleted = false;  // Changed from boolean to Boolean to allow nulls

    @Column(name = "done", nullable = true)
    private Boolean done;  // Changed from boolean to Boolean to allow nulls
    
    public ToDoItem() {}

    // Constructor with all fields
    public ToDoItem(int id, String title, String description, OffsetDateTime creationDate,
                   OffsetDateTime dueDate, State state, Sprint sprint, User user,
                   Project project, int storyPoints, String priority, boolean deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.state = state;
        this.sprint = sprint;
        this.user = user;
        this.project = project;
        this.storyPoints = storyPoints;
        this.priority = priority;
        this.deleted = deleted;
    }

    // Getters
    public int getID() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public OffsetDateTime getCreation_ts() { return creationDate; }
    public OffsetDateTime getDueDate() { return dueDate; }
    public State getState() { return state; }
    public Sprint getSprint() { return sprint; }
    public User getUser() { return user; }
    public Project getProject() { return project; }
    public void setDone(boolean done) { 
        this.done = done; 
    }

    //emtpy json getters
    @JsonGetter("storyPoints")
    public Integer getStoryPoints() {
        return storyPoints != null ? storyPoints : 0;
    }

    @JsonGetter("deleted")
    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    @JsonGetter("done")
    public Boolean isDone() {
        return done != null ? done : false;
    }

    @JsonGetter("priority")
    public String getPriority() {
        return priority != null ? priority : "";
    }




    // Setters
    public void setID(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCreation_ts(OffsetDateTime creationDate) { this.creationDate = creationDate; }
    public void setDueDate(OffsetDateTime dueDate) { this.dueDate = dueDate; }
    public void setState(State state) { this.state = state; }
    public void setSprint(Sprint sprint) { this.sprint = sprint; }
    public void setUser(User user) { this.user = user; }
    public void setProject(Project project) { this.project = project; }
    public void setStoryPoints(int storyPoints) { this.storyPoints = storyPoints; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", state=" + state +
                ", sprint=" + sprint +
                ", user=" + user +
                ", project=" + project +
                ", storyPoints=" + storyPoints +
                ", priority='" + priority + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
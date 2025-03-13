package com.springboot.MyTodoList.model;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.User;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.springboot.MyTodoList.model.BaseEntity;
import com.springboot.MyTodoList.model.Project;

import javax.persistence.*;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Entity
@Table(name = "todo")
public class ToDoItem {
    private static final Logger logger = LoggerFactory.getLogger(ToDoItem.class);

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

    @Column(name = "estimated_hours", nullable = true)
    private Integer estimatedHours;  // Changed from int to Integer to allow nulls

    @Column(name = "real_hours", nullable = true)
    private Integer realHours;  // Changed from int to Integer to allow nulls

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
    Project project, int storyPoints, String priority, boolean deleted, boolean done,
    Integer estimatedHours, Integer realHours) { // Add new parameters
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
    this.done = done;
    this.estimatedHours = estimatedHours; // Add new field
    this.realHours = realHours; // Add new field
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
    public Boolean isDone() {
        logger.debug("isDone() called, returning: {}", (done != null ? done : false));
        return done != null ? done : false;
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
    public Boolean getDone() {
        return done != null ? done : false;
    }

    @JsonGetter("priority")
    public String getPriority() {
        return priority != null ? priority : "";
    }

    @JsonGetter("estimatedHours")
public Integer getEstimatedHours() {
    return estimatedHours != null ? estimatedHours : 0;
}

@JsonGetter("realHours") 
public Integer getRealHours() {
    return realHours != null ? realHours : 0;
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
    public void setDone(boolean done) { 
        this.done = done; 
    }
    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    
    public void setRealHours(Integer realHours) {
        this.realHours = realHours;
    }
    

    @Override
    public String toString() {
        String str = "ToDoItem{" +
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
                ", estimatedHours=" + estimatedHours + // Add new field
                ", realHours=" + realHours + // Add new field 
                ", priority='" + priority + '\'' +
                ", deleted=" + deleted +
                ", done=" + done +
                '}';
        logger.debug("toString() called, returning: {}", str);
        return str;
    }
}
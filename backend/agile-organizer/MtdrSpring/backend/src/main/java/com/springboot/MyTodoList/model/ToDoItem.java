package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TODO")
public class ToDoItem extends BaseEntity{

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

}

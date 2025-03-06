package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "USER")
public class User extends BaseEntity{
    private int phone_number;
    private String name;
    private String description;
    private int workflow_priority;
    private OffsetDateTime dueDate;
    private State state;
    private Role role;
    private Team team;
}

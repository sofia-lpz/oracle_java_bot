package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PROJECT")
public class Project extends BaseEntity{

    private String name;
    private String description;
    private int workflow_priority;
    private OffsetDateTime dueDate;
    private State state;
    

}

package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PROJECTS")
public class Project extends BaseEntity{

    private String name;
    private String description;
    private OffsetDateTime due_date;

    @ManyToOne(optional = true)
    @JoinColumn(name = "state_id", nullable = true)
    private State state;

    

}

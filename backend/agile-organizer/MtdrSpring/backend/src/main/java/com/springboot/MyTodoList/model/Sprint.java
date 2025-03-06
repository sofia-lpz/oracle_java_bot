package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "SPRINT")
public class Sprint extends BaseEntity{

    private String name;
    private OffsetDateTime dueDate;
    private State state;
    private Project project;
    

}

package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "STATE")
public class State extends BaseEntity{

    private String name;
    private String description;
    private int workflow_priority;


}

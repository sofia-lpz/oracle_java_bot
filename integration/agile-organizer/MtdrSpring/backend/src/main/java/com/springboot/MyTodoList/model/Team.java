package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TEAM")
public class Team extends BaseEntity{
    private String name;
    private String description;


}

package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;

/*
    representation of the USER table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @Column(name = "PASSWORD")
    String password;
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "ROLE")
    private String role;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public User() {
    }

    public User(int ID, String password, String name, Team team)
    {
        this.ID = ID;
        this.password = password;
        this.name = name;
        this.team = team;
    }

    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
}
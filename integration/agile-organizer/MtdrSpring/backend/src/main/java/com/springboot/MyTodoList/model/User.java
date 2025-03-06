package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;

/*
    representation of the USER table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @Column(name = "NUMBER")
    String number;

    @Column(name = "PASSWORD")
    String password;

    public User(){}

    public User(int ID, String number, String password){
        this.ID=ID;
        this.number = number;
        this.password = password;
    }

    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number=number;
    }

    
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

}
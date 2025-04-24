package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;

    @JsonIgnore
    @Column(name = "PASSWORD")
    String password;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ROLE")
    private String role;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
    }

    public User(int ID, String password, String phoneNumber, String name, Team team) {
        this.ID = ID;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.team = team;
    }

    public int getID() {
        return ID;
    }

    public User setID(int ID) {
        this.ID = ID;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public User setTeam(Team team) {
        this.team = team;
        return this;
    }
}
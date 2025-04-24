package com.springboot.MyTodoList.model.dto;


public class RegisterUserDto {
    private String phoneNumber;
    
    private String password;
    
    private String name;

    private String role;

    public RegisterUserDto() {
    }
    public RegisterUserDto(String phoneNumber, String password, String name, String role) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
